package com.jpmorgan.sale.notification.message.saledata;

import java.util.Map;
import java.util.Set;

import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.model.SaleItem;

public interface Subject {
    public void addObserver(Observer... o);
    public void removeObserver(Observer o);
    public void notifyObservers(Sale sale) throws Exception;
    
    public Map<String, Set<Sale>> getSales();
    public Map<String, Set<SaleItem>> getSaleItens();
}
