package mx.com.sportsworld.sw.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import mx.com.sportsworld.sw.R;

public class CreateUserActivity extends Activity implements View.OnClickListener {

    private Button btnCrearCuenta;

    private Spinner ddlIdClub;

    private Spinner ddlTipoMember;

    private EditText txtNumMembresia;

    private EditText txtNumIntegrantes;

    private EditText txtCorreoE;

    private EditText txtConfCorreoE;

    private EditText txtUsuario;

    private EditText txtPassw;

    private EditText txtConfPassw;

    private UserParams mUserParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        ddlIdClub = (Spinner) findViewById(R.id.ddl_Club);

        ddlTipoMember = (Spinner) findViewById(R.id.ddl_TipoMembresia);

        txtNumMembresia = (EditText) findViewById(R.id.txtNumMembresia);

        txtNumIntegrantes = (EditText) findViewById(R.id.txtNumIntegrantes);

        txtCorreoE = (EditText) findViewById(R.id.txtCorreoE);

        txtConfCorreoE = (EditText) findViewById(R.id.txtConfCorreoE);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);

        txtPassw = (EditText) findViewById(R.id.txtPassw);

        txtConfPassw = (EditText) findViewById(R.id.txtConfPassw);

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(this);

    }

    private void loadCrearCuenta() {

        UserParams mUserParams = new UserParams();

        mUserParams.setIdClub(ddlIdClub.getSelectedItemId());

        mUserParams.setTipoMember(ddlTipoMember.getSelectedItemId());

        mUserParams.setIdMember(Integer.parseInt(txtNumMembresia.getText().toString()));

        mUserParams.setNumIntegrantes(Integer.parseInt(txtNumIntegrantes.getText().toString()));

        mUserParams.setCorreoE(txtCorreoE.getText().toString());

        mUserParams.setConfCorreoE(txtConfCorreoE.getText().toString());

        mUserParams.setUsuario(txtUsuario.getText().toString());

        mUserParams.setPassw(txtPassw.getText().toString());

        mUserParams.setConfPassw(txtConfPassw.getText().toString());

        Toast.makeText(getApplicationContext(), "carga cuenta" + " " + mUserParams.getIdMember() + " " + mUserParams.getNumIntegrantes(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCrearCuenta:
                loadCrearCuenta();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operation");
        }
    }
}
