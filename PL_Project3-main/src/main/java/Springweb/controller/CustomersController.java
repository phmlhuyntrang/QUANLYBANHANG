/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Springweb.controller;

import Springweb.entity.Customers;
import Springweb.repository.CustomersRepository;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author MaiThy
 */
@Controller
public class CustomersController {

    @Autowired
    private CustomersRepository reposity;

    @GetMapping("/customer/sign_up")
    public String register(Model m) {
        Customers cus = new Customers();
        m.addAttribute("customer", cus);
        return "customer/sign_up";
    }
    @PostMapping("/customer/save")
    public String save(Model model, @ModelAttribute("customer") Customers customer,
            RedirectAttributes redirAttrs) {
        Optional<Customers> optional = reposity.findByEmail(customer.getEmail());
        if (optional.isEmpty()) {
            try {
                reposity.save(customer);
                redirAttrs.addFlashAttribute("message_success", "Register Success");
            } catch (DataAccessException ex) {
                redirAttrs.addFlashAttribute("message_failed", "Register Failed");
            }
        }
        return "redirect:/customer/sign_up";
    }
    @PostMapping("/customer/checkEmail")
    public String checkEmail(@RequestParam("email") String email, ModelMap model) {
        Optional<Customers> optional = reposity.findByEmail(email);
        if (optional.isPresent()) {
            model.addAttribute("ERROR", "Login Failed");
        }
        return "/customer/log_in";
    }

    @PostMapping("/customer/update")
    public String update(Model model, @ModelAttribute("customer") Customers customer) {
        Optional<Customers> cus = reposity.findById(customer.getCustomerID());
        Customers c;
        c = cus.get();
        c.setAddress(customer.getAddress());
        c.setCity(customer.getCity());
        c.setFullname(customer.getFullname());
        reposity.save(c);

        return "redirect:/customer/all";

    }

    @GetMapping(value = {"customer/edit/{id}"})
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Customers> cus = reposity.findById(id);
        cus.ifPresent(customer -> model.addAttribute("customer", cus));
        //model.addAttribute("customer", cus);
        return "customer_edit";
    }

    @GetMapping("/customer/log_in")
    public String signInPage() {
        return "customer/log_in";
    }

    public String checkLogin(String email, String password) {
        Optional<Customers> optional = reposity.findByEmail(email);
        if (optional.isPresent() && optional.get().getPassword().equals(password)) {
            return optional.get().getFullname();
        }
        return null;
    }

    @PostMapping("/customer/checkLogin")
    public String checkLogin(@RequestParam("email") String email, @RequestParam("password") String password,
            ModelMap model, HttpSession session) {
        String fullname = checkLogin(email, password);
        if (fullname != null) {
            session.setAttribute("Fullname", fullname);
            Customers cus = reposity.findOneByFullname(fullname);
//            model.addAttribute("currentCustomer",cus);
            int id = cus.getCustomerID();
            session.setAttribute("currentCustomerID", id);
            return "redirect:/";
        } else {
            model.addAttribute("ERROR", "Login Failed");
        }
        return "/customer/log_in";
    }

    @GetMapping("/customer/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("Fullname");
        CartController.vegetableBuyCounter = 0;
        CartController.vegetableIDList.clear();
        return "/customer/log_in";
    }

}
