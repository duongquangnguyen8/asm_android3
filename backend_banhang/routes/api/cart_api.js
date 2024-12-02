const express=require('express');
const router=express.Router();
const Cart=require('../../models/cart');
const Product=require('../../models/products');

router.post('/add_cart',async(req,res)=>{
    try{
        const data=req.body;
        const newCart=new Cart({
            accountId:data.accountId,
            shippingAddress:data.shippingAddress,
            shippingPhone:data.shippingPhone,
        });
        const result=await newCart.save();
        res.status('200').json({
            status:'200',
            message:'Thành công add cart',
            data: result,
        });
        res.status
    }catch(error){
        console.log(error);
        res.status(500).json({
             status: "500",
              message: "Lỗi máy chủ",
               error: error.message,
             });
    }
});
router.get('/getAllCart',async(req,res)=>{
    try{

        const data=await Cart.find();
        res.status('200').json({
            status:'200',
            message: 'Thanhf công',
            data: data,
        })

        
    }catch(error){
        console.log(error);
    }
});
router.get('/getCartByidAccount/:idAccount',async(req,res)=>{
    try{
        const{idAccount}=req.params;
        const data=await Cart.findOne({accountId: idAccount});
        res.status('200').json({
            status:'200',
            message: 'Thanhf công',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
})
module.exports=router;