package eg.com.majesty.httpwww.majesty.Models
import com.google.gson.JsonArray
class BranchModel ( var CityID : Int ,
        var CityName : String ,
        var BranchesInCityList : JsonArray,
        var Selected : Boolean
)