package eg.com.majesty.httpwww.majesty.Models
class CartModel(var FoodMenuItemID:Int,
                var FoodMenuItemName:String,
                var FoodMenuName:String,
                var FoodMenuImageUrl:String,
                var Quantity:(Int),
                var ItemPrice:Float,
                var ItemTaxPercent:Float,
                var ItemTax:Float,
                var TotalAmount:Float
                )
