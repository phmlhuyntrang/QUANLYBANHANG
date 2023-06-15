/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Springweb.entity;

import Springweb.entity.Category;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Vegetable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int VegetableID;
    @Column(name="Vegetable_Name")
    private String vegetableName;
    @Column
    private String Unit;
    @Column
    private int Amount;
    @Column
    private String Image;
    @Column
    private float Price; 
    
    @Column
    private int catagoryID;                 
}