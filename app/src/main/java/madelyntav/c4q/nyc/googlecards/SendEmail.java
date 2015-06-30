package madelyntav.c4q.nyc.googlecards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elvisboves on 6/30/15.
 */
public class SendEmail extends Activity {

    @Bind(R.id.sendEmail) Button mSendEmail;



    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.send_email);
        ButterKnife.bind(this);

        mSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail();
            }
        });
    }

    protected void composeEmail(){
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.setType("*/*");
        if (intent1.resolveActivity(getPackageManager()) != null) {
            startActivity(intent1);
        }
    }


}
