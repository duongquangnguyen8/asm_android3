const express=require('express');
const router=express.Router();
const Bill=require('../../models/bill');

router.post('/add_bill',async(req,res)=>{
    try{
        const data=req.body;
        const newBill=new Bill({
            accountId:data.accountId,
            cartId:data.cartId,
            totalPrice:data.totalPrice,
            statusBill:data.statusBill,
            shippingName: data.shippingName,
            shippingAddress:data.shippingAddress,
            shippingPhone: data.shippingPhone,
        });
        const result=await newBill.save();
        res.status('200').json({
            status: '200',
            message: 'Thành công',
            data: result,
        });

    }catch(error){
        console.log(error);
    }
});
router.get('/getBillByIdAccount/:idAccount',async(req,res)=>{
    try{
        const {idAccount}=req.params;
        const data=await Bill.find({accountId: idAccount});
        res.status('200').json({
            status:'200',
            message: 'success',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
});
router.get('/getAllBill',async(req,res)=>{
    try{
        const data=await Bill.find();
        res.status('200').json({
            status: '200',
            message: 'success',
            data: data,
        })

    }catch(error){
        console.log(error);
    }
});
router.put("/updateBillById/:id",async (req,res)=>{
    try{
        const {id}=req.params;
        const updateData=req.body;
        const bill=await Bill.findById(id);
        Object.assign(bill,updateData);
        const result=await bill.save();
        res.status('200').json({
            status: '200',
            message: 'Get thành công',
            data: result,
        })

    }catch(error){
        console.log(error);
    }
})
module.exports=router;