package by.mopahta.qrscanner

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Hashtable

class GenerateActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var textEdit: TextInputEditText
    private lateinit var generateButton: Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate)


        backButton = findViewById(R.id.back)
        generateButton = findViewById(R.id.generateQR)
        textEdit = findViewById(R.id.qrText)
        image = findViewById(R.id.imageView)

        backButton.setOnClickListener { finish() }
        generateButton.setOnClickListener { onGenerateQR() }

    }

    private fun onGenerateQR() {
        if (textEdit.text.toString().trim() == "") {
            textEdit.error = "QR text is not supposed to be empty."
            return
        }

//        val outS = ByteArrayOutputStream()

//        io.github.g0dkar.qrcode.QRCode(textEdit.text.toString().trim()).renderShaded { cellData: QRCodeSquare, graphics ->
//            if (cellData.squareInfo.type != QRCodeSquareType.MARGIN && cellData.dark) {
//                if (cellData.row > cellData.col) {
//                    graphics.fill(Colors.BLUE)
//                } else {
//                    graphics.fill(Colors.RED)
//                }
//            } else {
//                graphics.fill(Colors.WHITE)
//            }
//        }.writeImage(outS)
//        io.github.g0dkar.qrcode.QRCode(textEdit.text.toString().trim()).render().writeImage(outS)

//        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
        val hints = Hashtable<EncodeHintType, String>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8";
        val bits = QRCodeWriter().encode(textEdit.text.toString().trim(), BarcodeFormat.QR_CODE, 512, 512, hints)
        val bm = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565).also {
            for (x in 0 until 512) {
                for (y in 0 until 512) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }

//        val renderOption = RenderOption()
//        renderOption.content = textEdit.text.toString().trim()
//        renderOption.size = 800 // size of the final QR code image
//        renderOption.borderWidth = 20 // width of the empty space around the QR code
//        renderOption.ecl = ErrorCorrectionLevel.M // (optional) specify an error correction level
//        renderOption.patternScale = 0.35f // (optional) specify a scale for patterns
//        renderOption.clearBorder = true // if set to true, the background will NOT be drawn on the border area
//
//        // on below line we are initializing our qr encoder
////        val bitmap: Bitmap = BitmapFactory.decodeByteArray(outS.toByteArray(), 0, outS.size())
//        val result = AwesomeQrRenderer.render(renderOption)
        image.setImageBitmap(bm)
    }
}