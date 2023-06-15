/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Springweb.controller;

import Springweb.entity.Customers;
import Springweb.entity.OrderDetail;
import Springweb.entity.Orders;
import Springweb.entity.Vegetable;
import Springweb.repository.CustomersRepository;
import Springweb.repository.OrderDetailRepository;
import Springweb.repository.OrderRepository;
import Springweb.repository.VegetableRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author MaiThy
 */
@Controller
public class CartController {
    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    VegetableRepository vegetableRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    
    ArrayList<Vegetable> orderVegetableList = new ArrayList();
    ArrayList<OrderDetail> orderDetailList = new ArrayList();
    private float totalPrice = 0;
    private Orders currentOrder;
    public static ArrayList<Integer> vegetableIDList = new ArrayList<>();
    public static int vegetableBuyCounter = 0;
    @GetMapping("/cart/checkout")
    public String receiveData(Model model, HttpSession session){
        model.addAttribute("totalPrice",totalPrice);
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        
        long millis=System.currentTimeMillis();
        currentOrder = new Orders();
        currentOrder.setCustomerID((Integer) session.getAttribute("currentCustomerID"));
        currentOrder.setDate(new Date(millis));
        currentOrder.setTotal(totalPrice);
        currentOrder.setNote("");
        
//        SET VALUE INFO CUSTOMER
        Optional<Customers> cus = customersRepository.findById((Integer)session.getAttribute("currentCustomerID"));
        model.addAttribute("currentCustomer", cus.get());
        
        return "cart/checkout";
    }
    
    @GetMapping("/cart")
    public String cartPage(HttpSession session) {
        int numberOfVegetable = CartController.vegetableBuyCounter;
        for(Integer id : CartController.vegetableIDList) {
            Vegetable vegetable = vegetableRepository.findById(id).orElse(null);
            if(!orderVegetableList.contains(vegetable)) {
                orderVegetableList.add(vegetable);
            }
        }
        
        session.setAttribute("vegetableCounter", numberOfVegetable);
        session.setAttribute("vegetables", orderVegetableList);
        return "cart/index";
    }
    
    @GetMapping("/deleteOrderDetail")
    public String deleteOrderDetail(@RequestParam("id") int vegetableID) {
        if(CartController.vegetableIDList.contains(vegetableID)) {
            for (Iterator<Integer> iterator = CartController.vegetableIDList.iterator(); iterator.hasNext(); ) {
                Integer value = iterator.next();
                if(value == vegetableID) {
                    iterator.remove();
                }
            } 
            for (Iterator<Vegetable> iterator = orderVegetableList.iterator(); iterator.hasNext(); ) {
                Vegetable value = iterator.next();
                if(value.getVegetableID() == vegetableID) {
                    iterator.remove();
                }
            }
        }        
        CartController.vegetableBuyCounter--;
        return "redirect:/cart";
    }
    
    @PostMapping("/processOrderDetail")
    @ResponseBody
    public void processOrderDetail(@RequestBody String jsonString) {
        Gson gson = new Gson();
        JsonObject recevieData = gson.fromJson(jsonString,JsonObject.class);
        totalPrice = recevieData.get("totalPrice").getAsFloat();
        JsonArray jsonArray = recevieData.get("orderDetail").getAsJsonArray();
        if(jsonArray.size() != 0) {
            for(JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                OrderDetail detail = new OrderDetail();
                detail.setVegetableID(object.get("VegetableID").getAsInt());
                detail.setQuantity(object.get("Quantity").getAsInt());
                detail.setPrice(object.get("Price").getAsFloat());
                orderDetailList.add(detail);
            }
        }
        else {
            System.out.println("OrderDetail have no element");
        }
        for(OrderDetail detail : orderDetailList) {
            System.out.println(detail.toString());
        }
    }
    
    @GetMapping("/createOrder")
    public String createOrder(Model model) {
        if(orderDetailList.isEmpty()) {
            return "redirect:/cart";
        }
        currentOrder = orderRepository.save(currentOrder);
        int orderID = currentOrder.getOrderID();
        for(OrderDetail detail : orderDetailList) {
            detail.setOrderID(orderID);
            detail = orderDetailRepository.save(detail);
        }
        CartController.vegetableBuyCounter = 0;
        totalPrice = 0;
        CartController.vegetableIDList = new ArrayList();
        orderVegetableList = new ArrayList();
        orderDetailList = new ArrayList();
        model.addAttribute("statusOrder","success");
        return "redirect:/";
    }
    @GetMapping("/increment")
    public String incrementCounter(@RequestParam("id") Integer vegetableID, HttpServletRequest request) {
        if(!CartController.vegetableIDList.contains(vegetableID)) {
            CartController.vegetableBuyCounter++;
            CartController.vegetableIDList.add(vegetableID);
        }
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}