package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.lncanswer.ExceptionHandler.CustmoerException;
import com.lncanswer.entitly.AddressBook;
import com.lncanswer.entitly.Orders;
import com.lncanswer.entitly.ShoppingCart;
import com.lncanswer.entitly.User;
import com.lncanswer.mapper.OrderMapper;
import com.lncanswer.utils.BaseContext;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Orders> implements com.lncanswer.Service.OrderService {

   @Autowired
    ShoppingCartService shoppingCartService;

   @Autowired
   UserServiceImple userServiceImple;

   @Autowired
   AddressBookService addressBookService;

   @Autowired
   OrderDetail orderDetailService;

    /*
    查询订单分页
     */
    @Override
    public Page selectPage(Integer page, Integer pageSize) {
        Page page1 = new Page(page,pageSize);
        this.page(page1);
        return page1;
    }

    /*
    提交订单
     */
    @Override
    @Transactional
    public void postOrder(Orders orders) {
        //要设置当前订单用户的id 和下单时间
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        //通过用户id查找用户购物车中需要支付的金额 再将其赋值给当前订单属性
        LambdaQueryWrapper<ShoppingCart> lam = new LambdaQueryWrapper<>();
        lam.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        //sql:select * from shopping_cart where user_id = ?       ? =BaseContext.getCurrentId
         List<ShoppingCart> list = shoppingCartService.list(lam);

         //创建一个变量来统计查询列表中的每一个菜品的金额
        //这里我们使用final 数组来保存菜品金额， 数组大小为1
        final BigDecimal[] orderAmount = { BigDecimal.ZERO };
         if (list != null)
         {
             //将订单添加到order表中
             orders.setStatus(4);

             //查询用户信息
             User user = userServiceImple.getById(BaseContext.getCurrentId());
             //为order对象赋值用户名字属性 和用户电话属性
             orders.setUserName(user.getName());
             orders.setPhone(user.getPhone());

             //查询地址信息
             AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
             //为orders对象设置地址属性值
             orders.setAddress(addressBook.getDetail());


            List<com.lncanswer.entitly.OrderDetail> list1  =  list.stream().map((item) ->{

                 //BigDecimal类型的变量使用add将两个BigDecimal类型的变量添加
                //Lambda表达式内部使用的局部变量必须是final或者是effectively final，
                // 这意味着变量的值不能在Lambda表达式内部被修改。
                //System.out.println(item.getAmount().toString());
                //orderAmount = orderAmount.add(new BigDecimal(item.getAmount().toString()));
                // System.out.println(orderAmount);
                System.out.println(item.getAmount().toString());
                orderAmount[0] = orderAmount[0].add(new BigDecimal(item.getAmount().toString()));
                System.out.println(orderAmount[0]);

                //创建一个订单明细数据
                com.lncanswer.entitly.OrderDetail detail = new com.lncanswer.entitly.OrderDetail();

                //为每一个订单明细数据设置初始值
                detail.setName(item.getName());
                detail.setImage(item.getImage());
                detail.setAmount(item.getAmount());
                detail.setDishId(item.getDishId());
                detail.setAmount(item.getAmount());
                LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Orders::getUserId,item.getUserId());
                //select * from orders where user_id = ?
                Orders orders1 = this.getOne(queryWrapper);

                //需要修改 暂时为其赋值一个购物车菜品id
                detail.setOrderId(item.getId());


                 return detail;
             }).collect(Collectors.toList());

            //设置订单数据金额
             orders.setAmount(orderAmount[0]);


             boolean num = this.save(orders);
             //像订单明细表中插入多条数据
             orderDetailService.saveBatch(list1);
             if(num)
             {
                 //下单成功 将购物车中的数据删除掉
                 LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
                 wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
                 //sql: delete from shopping_cart where user_id = ?
                 shoppingCartService.remove(lam);
             }
             return;
         }
         throw new CustmoerException("列表不能为空！");

    }
}
