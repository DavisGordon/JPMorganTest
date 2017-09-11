/**
 * 
 */
package com.jpmorgan.sale.notification.message.saledata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.model.SaleItem;

/**
 * @author Davis Gordon
 */
public class SaleData implements Subject {
    	
	// Sales recorded
	private Map<String, Set<Sale>> sales = new HashMap<>();
	private Map<String, Set<SaleItem>> saleItens = new HashMap<>();
	
	
	// Sales observable agents
    private List<Observer> observers = null;
    
    public SaleData() {
        observers = new ArrayList<Observer>();
    }
    
    public void addObserver(Observer... o) {
        observers.addAll(Arrays.asList(o));
    }
    
    public void notifyObservers(Sale sale) throws Exception {
        for(Observer o: observers) {
            o.update(sale);
        }
    }
    
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    
    public void setSale(Sale sale) throws Exception {
        
    	synchronized (sale) {
    		
    		String type = sale.getProduct().getType();
        	if(this.sales.containsKey(type))
        		this.sales.get(type).add(sale);
        	else
        		this.sales.put(type, new HashSet<>(Arrays.asList(sale)));
        	
        	for(int x = 1; x<=sale.getUnits(); x++){
        		if(this.saleItens.containsKey(type))
            		this.saleItens.get(type).add(new SaleItem(sale.getProduct().getType(), sale.getProduct().getValue()));
            	else
            		this.saleItens.put(type, new HashSet<>(Arrays.asList(new SaleItem(sale.getProduct().getType(), sale.getProduct().getValue()))));
        	}
        	notifyObservers(sale);
		} 
    }
    
    public Map<String, Set<Sale>> getSales() {
		return sales;
	}
    
    public Map<String, Set<SaleItem>> getSaleItens() {
		return saleItens;
	}
    
}
