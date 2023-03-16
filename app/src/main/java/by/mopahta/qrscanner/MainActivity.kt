package by.mopahta.qrscanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scanResult: TextView
    private lateinit var openButton: Button
    private lateinit var copyButton: Button
    private lateinit var generateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)
        scanResult = findViewById(R.id.scanRes)
        openButton = findViewById(R.id.openInButton)
        copyButton = findViewById(R.id.copyButton)
        generateButton = findViewById(R.id.generateQR)

        openButton.setOnClickListener { onOpenButtonClick() }
        copyButton.setOnClickListener { onCopyButtonClick() }
        generateButton.setOnClickListener { onGenerateButtonClick() }

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                scanResult.text = it.text
                openButton.isEnabled = URLUtil.isValidUrl(it.text)
                copyButton.isEnabled = true


            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun onOpenButtonClick() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(scanResult.text.toString()))
        ContextCompat.startActivity(this, browserIntent, null)
    }

    private fun onCopyButtonClick() {
        copyToClipboard(scanResult.text)
    }

    private fun copyToClipboard(text: CharSequence) {
        val clip = ClipData.newPlainText("qrResult", text)
        ContextCompat.getSystemService(this, ClipboardManager::class.java)?.setPrimaryClip(clip)
    }

    private fun onGenerateButtonClick() {
        val intent = Intent(this, GenerateActivity::class.java)
        createHeaderForResult.launch(intent)
    }

    private val createHeaderForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {

            val resData: Intent = result.data!!

        }
    }
}