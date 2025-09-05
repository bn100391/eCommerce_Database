SELECT Product."ProductName ", Product.Price
    FROM Customer JOIN 'Order' JOIN Purchases JOIN Product
    ON Customer.Customer_ID = 'Order'.Customer_ID 
    AND 'Order'.O_ID = Purchases.O_ID
    AND Purchases.Product_ID = Product.Product_ID
    WHERE Customer.Customer_ID = 2
    AND Product.Price >= 6
    GROUP BY Product.Product_ID