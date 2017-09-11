package com.jpmorgan.sale.notification.message.saledata;

import com.jpmorgan.sale.notification.message.model.Sale;

public interface Observer {
    public void update(Sale sale) throws Exception;
}
