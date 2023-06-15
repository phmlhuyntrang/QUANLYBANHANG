/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Springweb.controller;

import Springweb.entity.Category;
import Springweb.entity.Vegetable;
import Springweb.repository.CategoryRepository;
import Springweb.repository.VegetableRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author MaiThy
 */
@Controller
public class VegetableController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VegetableRepository vegetableRepository;

    

    @GetMapping(value = {"/vegetable/search"})
    public String searchVegetable(@RequestParam("name") String name, @RequestParam("selectCategory") String selectCategory, Model model, HttpSession session) {
        Iterable<Category> listCategory = categoryRepository.findAll();
        model.addAttribute("dataCategory", listCategory);
        if (selectCategory.equals("All Categories")) {
            List<Vegetable> vegetable = vegetableRepository.findByVegetableNameContaining(name);
            model.addAttribute("listSearchVegetable", vegetable);
        } else {
            int categoryID = categoryRepository.findOneByName(selectCategory).getCatagoryID();
            List<Vegetable> vegetable = vegetableRepository.findByVegetableNameContainingAndCatagoryID(name, categoryID);
            model.addAttribute("listSearchVegetable", vegetable);
        }
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        return "vegetable/search";
    }

    @GetMapping("/vegetable")
    public String vegetablePage(Model model, HttpSession session) {
        Iterable<Vegetable> listVegetable;
        listVegetable = vegetableRepository.findAll();
        model.addAttribute("listSearchVegetable", listVegetable);
        Iterable<Category> listCategory = categoryRepository.findAll();
        model.addAttribute("listCategory", listCategory);
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        return "vegetable/index";
    }

    @GetMapping("/vegetable{name}")
    public String getNameCategory(@PathVariable(value = "name") String name, Model model, HttpSession session) {
        System.out.println(name);
        Iterable<Vegetable> listVegetable;
        if (name.equals("all")) {
            listVegetable = vegetableRepository.findAll();
        } else {
            Category c = categoryRepository.findOneByName(name);
            listVegetable = vegetableRepository.findByCatagoryID(c.getCatagoryID());
        }
        model.addAttribute("listSearchVegetable", listVegetable);
        Iterable<Category> listCategory = categoryRepository.findAll();
        model.addAttribute("listCategory", listCategory);
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        return "vegetable/index";
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/best_seller")
    public String bestSeller(Model model, HttpSession session) {
        String sql = """
                     SELECT VegetableID
                     FROM `orderdetail`
                     GROUP BY VegetableID
                     ORDER BY SUM(Quantity) DESC
                     LIMIT 8 """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Iterable<Vegetable> vegetables;
        ArrayList<Integer> ids = new ArrayList<>();
        for (Map<String, Object> row : rows) {

            ids.add(((Integer) row.get("VegetableID")));
        }
        vegetables = vegetableRepository.findAllById(ids);
        model.addAttribute("vegetables", vegetables);
        session.setAttribute("vegetableCounter", CartController.vegetableBuyCounter);
        return "best_seller/index";
    }
}
