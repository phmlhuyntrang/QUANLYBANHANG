/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Springweb.controller;
import Springweb.entity.Category;
import Springweb.repository.CategoryRepository;
import Springweb.entity.Vegetable;
import Springweb.repository.VegetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author MaiThy
 */
@Controller
public class HomeController {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VegetableRepository vegetableRepository;
    
    @GetMapping("/")
    public String homePage(Model model, HttpSession session) {
        Iterable<Vegetable> listVegetable = vegetableRepository.findAll();
        model.addAttribute("dataVegetable", listVegetable);

        Iterable<Category> listCategory = categoryRepository.findAll();
        model.addAttribute("dataCategory", listCategory);
        
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        return "index";
    }
    
    
    

    

}
