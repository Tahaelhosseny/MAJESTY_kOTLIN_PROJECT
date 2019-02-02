package eg.com.majesty.httpwww.majesty.Models

class UpcommingOrdersModel
(
        var OrderNo: Int,
        var OrderDate: String,
        var OrderImageUrl: String,
        var IsUpComming: Boolean,
        var IsConfirmed: Boolean,
        var ConfirmDatetime: String,
        var ExpectedDeliveryMinues: Float,
        var TotalBeforeTax: Float,
        var TotalTax: Float,
        var DeliveryFees: Float,
        var Discount: Float,
        var Total: Float,
        var Rating: Int,
        var CommentAfterDelivery: String,
        var StatusDesc: String,
        var OrderNotesByCustomer: String,
        var UserAddressID: Int,
        var UserAddressLine1: String,
        var UserAddressLine2: String,
        var UserAddressLine3: String,
        var UserAddressLine4: String,
        var COnfirmDateTimeTS: String ,
        var OrderDateTS: String
)