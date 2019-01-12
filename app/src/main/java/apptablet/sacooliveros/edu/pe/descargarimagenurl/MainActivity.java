package apptablet.sacooliveros.edu.pe.descargarimagenurl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://lh3.googleusercontent.com/-mOVh4Mmrq30/AAAAAAAAAAI/AAAAAAAAAB4/pyuzEt7HQYI/s96-c/photo.jpg";

    private ImageView imgImagen;
    private Button btnGuardar;

    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgImagen = (ImageView)findViewById(R.id.imagen);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);


        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(URL);


        btnGuardar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bitmap imagen = ((BitmapDrawable)imgImagen.getDrawable()).getBitmap();

                String ruta = guardarImagen(getApplicationContext(), "photos", imagen);

                Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();
            }

        });
    }






    private class CargaImagenes  extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Cargando Imagen");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground" , params.toString());
            String url = params[0];
             imagen = descargarImagen(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

           //imgImagen.setImageBitmap(result);
            pDialog.dismiss();
        }

    }

    private Bitmap descargarImagen (String imageHttpAddress){
        //URL
        java.net.URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }



    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir(nombre, Context.MODE_PRIVATE);
        File myPath = new File(dirImages, "nombre" + ".png");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();

    }
}

