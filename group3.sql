SELECT SUM(Product.Price) AS Total_Sales
    FROM Purchases JOIN Product 
    ON Purchases.Product_ID = Product.Product_ID
    WHERE O_ID = (
        SELECT O_ID
        FROM "Order"
        WHERE OrderDate = '20200101'
        );