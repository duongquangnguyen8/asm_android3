const express=require('express');
const router=express.Router();
const CartDetail=require('../../models/cartDetail');
router.post('/add_cartDetail',async (req,res)=>{
    try{
        const data=req.body;
        const newCartDetail=new CartDetail({
            cartId: data.cartId,
            productId: data.productId,
            quantity: data.quantity,
            price: data.price,
        });
        const result=await newCartDetail.save();
        res.status('200').json({
            status:'200',
            message: 'Thành công',
            data: result,
        })

    }catch(error){
        console.log(error);
    }
});
router.put('/updateCartDetailById/:id',async (req,res)=>{
    try{
        const {id}=req.params;
        const updateData=req.body;
        const cartDetail=await CartDetail.findById(id);
        Object.assign(cartDetail,updateData); //lưu sự thay đổi
        const result=await cartDetail.save(); //trả về kq
        res.status('200').json({
            status: '200',
            message: "success",
            data: result
        });
    }catch(error){
        console.log(error);
    }
   
});
router.get('/getCartDetailByidCartAndidProduct/:idCart/:idProduct',async(req,res)=>{
    try{
        //cach1
        // const {idCart}=req.params.idCart;
        // const {idProduct}=req.params.idProduct;

        const {idCart,idProduct}=req.params;
        const data=await CartDetail.findOne({cartId: idCart,productId: idProduct });
        res.status('200').json({
            status: '200',
            message: 'success',
            data: data
        })

    }catch(error){
        console.log(error);
    }
});
router.get('/getCartDetailByIdCart/:idCart',async(req,res)=>{
    try{
        const {idCart}=req.params;
        const data=await CartDetail.find({cartId: idCart});
        res.status('200').json({
            status:'200',
            message: 'success',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
});
router.delete('/deteCartDetailById/:id',async (req,res)=>{
   try{
        const {id}=req.params;
        const data=await CartDetail.findByIdAndDelete(id);
        res.status('200').json({
            status:'200',
            message: 'success',
            data: data,
        })
   }catch(error){
    console.log(error);
   }
});
router.delete('/deleteCartDetailByIdProduct/:idProduct',async(req,res)=>{
    try{
        const {idProduct}=req.params;
        console.log(idProduct);
        const data=await CartDetail.findOneAndDelete({productId: idProduct});
        if (!data) { 
            return res.status(404).json({
                 status: '404',
                  message: 'Cart detail not found',
                 });
            }
        res.status('200').json({
            status:'200',
            message:'success',
            data: data,
        })

    }catch(error){
        console.log(error);
    }
})
module.exports=router;