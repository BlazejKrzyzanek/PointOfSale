# Point Of Sale
Sample task for Java Developer candidate

<em>Barcodes existing in database: 999999000 - 999999999</em>

![](https://s10.ifotos.pl/img/Bez-nazwy_qwqrews.jpg "Window")

Implement a simple point of sale.

<b>Assume you have:</b>
 - one input device: bar codes scanner
 - two output devices: LCD display and printer
 
<b>Implement:</b>
 - single product sale: products bar code is scanned and:
 - if the product is found in products database then it's name and price is printed on LCD
display
 - if the product is not found then error message 'Product not found' is printed on LCD
display
 - if the code scanned is empty then error message 'Invalid bar-code' is printed on LCD
display
 - when 'exit' is input then receipt is printed on printer containing a list of all previously
scanned items names and prices as well as total sum to be paid for all items; the total sum
is also printed on LCD display
