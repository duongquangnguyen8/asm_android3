const mongoose = require("mongoose");
const mongoURI = "mongodb://127.0.0.1:27017/ASM_ANDROID3";

const connect = async () => {
    try {
        await mongoose.connect(mongoURI, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
        });
        console.log("Kết nối thành công");
    } catch (error) {
        console.log("Kết nối thất bại", error);
    }
}

module.exports = { connect };
