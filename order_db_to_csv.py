#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 30 11:49:38 2022

@author: Nebiyou Ersabo
"""


    
    
    
import csv
from datetime import timedelta
import random
import datetime

with open('order4.csv', 'w', newline='') as file:
    cursor = csv.writer(file);
    
    cursor.writerow(['Date', 'Server_ID', 'Total Amount', 'Entree_ID', 'Toppings_ID', 'Dressings_ID', 'Starters_ID', 'Drinks_ID'])
    
    entrees = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
    
    toppings = [0,1,2,3,4,5,6,7,8]
    
    dressing = [0,1,2,3,4,5,6,7,8]
    
    starters = [0,1,2,3,4]
    
    drinks = [0, 1, 2, 3]
    
    servers= [1, 2, 3, 4, 5, 6]
    
    total = 0
    
    
    date = datetime.date(2022,8,1)
    
    saturday_cap=5000
    
    day_cap = 2500
     
    #order_id=1
    for day in range (0,20):
        total = 0
        day_cap=2500
        if(day%7==0):
              day_cap = saturday_cap     
        
        while (total < day_cap):
            inner_total = 7.69
            row = [date, random.choice(servers), inner_total, random.choice(entrees)] 
                
            topping_list = [0,0,0,0]
            for i in range(0,4):
                topping_list[i] = random.choice(toppings);
                
            
            row+= [topping_list, random.choice(dressing), random.choice(starters), random.choice(drinks)]
                
            if(row[-1] != 0):
                row[2]+=1.5
                inner_total+=1.5
                    
            if(row[3] == 20):
                row[2] = 8.99
                inner_total=8.99
            
            cursor.writerow(row)
        
            total+=inner_total
            inner_total=0;
            #order_id+=1
            
            
        
        new_date = datetime.timedelta(1)
        date+=new_date
        #print(date, total)
                    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    