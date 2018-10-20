package wannabit.io.eoswallet.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.model.WBUser;

public class ReceiveActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private TextView mAccountTv;
    private ImageView mQrImage;
    private Button mBtnShare;
    private RelativeLayout mBtnCopy;

    private WBUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        mUser = getBaseDao().onSelectById(getIntent().getLongExtra("accountId", -1));

        mToolbar = findViewById(R.id.toolbar);
        mAccountTv = findViewById(R.id.accountName);
        mQrImage = findViewById(R.id.accountQr);
        mBtnShare = findViewById(R.id.btn_Share);
        mBtnCopy = findViewById(R.id.btn_copy);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnShare.setOnClickListener(this);
        mBtnCopy.setOnClickListener(this);

        if(mUser != null) {
            onInitViews();
        } else {
            onBackPressed();
        }

    }


    private void onInitViews() {
        mAccountTv.setText(mUser.getAccount());
        generateRQCode(mUser.getAccount());

    }

    @Override
    public void onClick(View view) {
        if(view.equals(mBtnCopy)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(mUser.getAccount(), mUser.getAccount());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ReceiveActivity.this, R.string.str_copyed_account_msg, Toast.LENGTH_SHORT).show();

        } else if (view.equals(mBtnShare)) {
            Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, mUser.getAccount() );
            startActivity(Intent.createChooser(intent2, "Share"));
        }
    }


    private void generateRQCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 480, 480));
            mQrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
