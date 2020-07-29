package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.Salary;
import org.springframework.stereotype.Component;

@Component
public class SalaryCreator {
    public Salary createSalary(){
        return new Salary();
    }
}
