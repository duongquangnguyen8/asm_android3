const express = require("express");
const router = express.Router();
const Product = require("../../models/products");
const Category = require("../../models/category");

router.post("/add_product", async (req, res) => {
  try {
    const data = req.body;
    const newProduct = new Product({
      productName: data.productName,
      description: data.description,
      price: data.price,
      image: data.image,
      categoryId: data.categoryId,
    });
    const result = await newProduct.save();
    res.status("200").json({
      status: "200",
      message: "Thành công add product",
      data: result,
    });
  } catch (error) {
    console.log(error);
  }
});
router.get("/get_all_product", async (req, res) => {
  try {
    const data = await Product.find();
    res.status("200").json({
      status: "200",
      message: "Get thành công",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});
router.get("/get_productById/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const data = await Product.findById(id);
    res.status("200").json({
      status: "200",
      message: "Get thành công",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});
router.put("/updateProductById/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const updateData = req.body;
    const product = await Product.findById(id);
    Object.assign(product, updateData);
    const result = await product.save();
    res.status("200").json({
      status: "200",
      message: "Get thành công",
      data: result,
    });
  } catch (error) {
    console.log(erorr);
  }
});
router.delete("/delete_productById/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const data = await Product.findOneAndDelete(id);
    res.status("200").json({
      status: "200",
      message: "success",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});
router.get("/search", async (req, res) => {
  try {
    const { productName } = req.query;
    if (!productName || typeof productName !== "string") {
      return res
        .status(400)
        .json({ status: "400", message: "Invalid product name" });
    }
    const products = await Product.find({
      productName: { $regex: productName, $options: "i" },
    });
    res.status(200).json({ status: "200", message: "success", data: products });
  } catch (error) {
    console.error("Error:", error);
    res
      .status(500)
      .json({
        status: "500",
        message: "Internal Server Error",
        error: error.message,
      });
  }
});
module.exports = router;
