package com.example.user.zoom3_asus;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener{

    TextView tvAction,tvIndex,tvComodin0,tvData1,tvData2,tvData3,tvData4,tvData5;
    TextView tvData6,tvData7,tvData8,tvData9,tvData10,tvData11,tvData12,tvData13,tvData14,tvData15;
    TextView tvData16,tvData17,tvData18,tvData19,tvData20,tvData21,tvData22,tvData23,tvData24;
    List<TextView> tvData;
    LinearLayout linearLayoutTouchable,containerForRedBox,redBox;
    float Scale_OnStartScaling=1;

    CountDownTimer WatchDog;
    //SourcePoints[]: puntos usados para guardar temporalmente los puntos que
    //se van a usar como origen del escalado. (Surgidos ante la duda de
    //si es mejor usar los puntos originales del evento ME.TouchDown
    //o usar los puntos del último cicle ME.TouchMOVE)
    PointF[] SourcePoints=new PointF[2];

    Timer timer;
    private final int interval = 1000; // 1 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            Toast.makeText(MainActivity.this, "C'Mom no hands!", Toast.LENGTH_SHORT).show();
        }
    };
    DualDrag dualDrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAction=(TextView)findViewById(R.id.tvAction);
        tvIndex=(TextView)findViewById(R.id.tvIndex);
        tvComodin0=(TextView)findViewById(R.id.tvComodin0);
        tvData=new ArrayList<TextView>();
        tvData.add( tvData1=(TextView)findViewById(R.id.tvData1));
        tvData.add(tvData2=(TextView)findViewById(R.id.tvData2));
        tvData.add(tvData3=(TextView)findViewById(R.id.tvData3));
        tvData.add(tvData4=(TextView)findViewById(R.id.tvData4));
        tvData.add(tvData5=(TextView)findViewById(R.id.tvData5));
        tvData.add(tvData6=(TextView)findViewById(R.id.tvData6));
        tvData.add(tvData7=(TextView)findViewById(R.id.tvData7));
        tvData.add(tvData8=(TextView)findViewById(R.id.tvData8));
        tvData.add(tvData9=(TextView)findViewById(R.id.tvData9));
        tvData.add(tvData10=(TextView)findViewById(R.id.tvData10));
        tvData.add(tvData11=(TextView)findViewById(R.id.tvData11));
        tvData.add(tvData12=(TextView)findViewById(R.id.tvData12));
        tvData.add(tvData13=(TextView)findViewById(R.id.tvData13));
        tvData.add(tvData14=(TextView)findViewById(R.id.tvData14));
        tvData.add(tvData15=(TextView)findViewById(R.id.tvData15));
        tvData.add(tvData16=(TextView)findViewById(R.id.tvData16));
        tvData.add(tvData17=(TextView)findViewById(R.id.tvData17));
        tvData.add(tvData18=(TextView)findViewById(R.id.tvData18));
        tvData.add(tvData19=(TextView)findViewById(R.id.tvData19));
        tvData.add(tvData20=(TextView)findViewById(R.id.tvData20));
        tvData.add(tvData21=(TextView)findViewById(R.id.tvData21));
        tvData.add(tvData22=(TextView)findViewById(R.id.tvData22));
        tvData.add(tvData23=(TextView)findViewById(R.id.tvData23));
        tvData.add(tvData24=(TextView)findViewById(R.id.tvData24));

        SourcePoints[0]=new PointF();
        SourcePoints[1]=new PointF();

        linearLayoutTouchable=(LinearLayout)findViewById(R.id.LinearLayoutTouchable);
        containerForRedBox=(LinearLayout)findViewById(R.id.container_for_redBox);
        redBox=(LinearLayout)containerForRedBox.findViewById(R.id.redBox);
        dualDrag= new DualDrag();

        linearLayoutTouchable.setOnTouchListener(this);

        WatchDog=new CountDownTimer(10000000, 100) {
            public void onTick(long millisUntilFinished) {
                tvComodin0.setText("" + millisUntilFinished / 1000);
                tvData1.setText("moving="+dualDrag.moving);
                tvData2.setText("scaling="+dualDrag.scaling);
                tvData2.setText("scaling="+dualDrag.scaling);
                tvData3.setText("pCount="+dualDrag.pointerCount);
                if(dualDrag.pointerCount>0)
                {
                    tvData4.setText("Last[0]=");
                    tvData5.setText("X=" + dualDrag.LastX[0]);
                    tvData6.setText("Y=" + dualDrag.LastY[0]);
                    tvData10.setText("Touch[0]=");
                    tvData11.setText("X=" + dualDrag.TouchX[0]);
                    tvData12.setText("Y=" + dualDrag.TouchY[0]);
                }
                else
                {
                    tvData4.setText("Last[0]=");
                    tvData5.setText("X= --");
                    tvData6.setText("Y= --");
                    tvData10.setText("Touch[0]=");
                    tvData11.setText("X= --");
                    tvData12.setText("Y= --");

                }
                if(dualDrag.pointerCount>1) {
                    tvData7.setText("Last[1]");
                    tvData8.setText("X=" + dualDrag.LastX[1]);
                    tvData9.setText("Y=" + dualDrag.LastY[1]);
                    tvData13.setText("Touch[1]=");
                    tvData14.setText("X=" + dualDrag.TouchX[1]);
                    tvData15.setText("Y=" + dualDrag.TouchY[1]);
                }
                else
                {
                    tvData7.setText("Last[1]");
                    tvData8.setText("X= --");
                    tvData9.setText("Y= --");
                    tvData13.setText("Touch[1]=");
                    tvData14.setText("X= --");
                    tvData15.setText("Y= --");
                }

            }

            public void onFinish() {
                tvComodin0.setText("done!");
            }
        }.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent ME) {
        int local_action;
        int local_index;
        Rect ValidRegion=new Rect();
        int[] locationOnScreen;
        local_index=ME.getActionIndex();
        local_action=ME.getActionMasked();

        //tvAction.setText(""+MotionEvent.actionToString(local_action));
        tvIndex.setText("Index="+local_index);

        //para delimitar el touch a la zona deseada, hay que tener en cuenta que las
        //métodos view.getTop(),view.getLeft()...etc tienen el origen de coordenadas
        // en el view Padre, mientras que el evento Touch ( ME.getX(index) ) tiene
        // el origen de coordenadas en la ventana, (justo bajo el titulo de la app)
        //Lo que implica un cambio de coordenadas.

        //para conocer las coordenadas del "view" con origen en la esquina de la pantalla
        //redBox.getGlobalVisibleRect(ValidRegion);

        //All these measurement methods return sizes in pixels( px ), not density-pixels ( dp ). If you want to convert it you can get the density by calling:
        //And then divide the values you get with the provided density, for example:
        //float density = getResources().getDisplayMetrics().density;
        //int redBoxX2 = (int)(redBoxX * density);
        //int redBoxY2 = (int)(redBoxY * density);

        //para conocer los margenes del "view RedBox" con origen en la esquina justo debajo de la app
        float redBoxX=containerForRedBox.getX()+ redBox.getX();
        float redBoxY=containerForRedBox.getY()+redBox.getY();
        int redBoxRight2 =(int)redBoxX+ (int)(redBox.getRight()*redBox.getScaleX());
        int redBoxBottom2 =(int)redBoxY+ (int)(redBox.getBottom()*redBox.getScaleY());
        DebugGrid(5,"Left-UP",""+redBoxX,""+redBoxY);
        DebugGrid(6,"Right-Do",""+redBoxRight2,""+redBoxBottom2);
        DebugGrid(7,"ME (X,Y)",""+ME.getX(),""+ME.getY());
        ValidRegion=new Rect((int)redBoxX,
                (int)redBoxY,
                redBoxRight2,
                redBoxBottom2);

        //Clase dualDrag (@ORParga) para facilitar el manejo de los eventos "Touch"
        dualDrag.MovionEventFilter(ME,ValidRegion);

        if(dualDrag.StartScaling)Scale_OnStartScaling=view.getScaleX();
        if(dualDrag.moving)
        {
            redBox.setX(redBox.getX()+dualDrag.TouchX[0]-dualDrag.LastX[0]);
            redBox.setY(redBox.getY()+dualDrag.TouchY[0]-dualDrag.LastY[0]);
        }
        if (dualDrag.scaling)
        {
            //O=Origen
            //Co= Contenedor
            //V=View
            //Ce=Center
            //F=Finguer

            //_1_ finguer 1
            //_2_ finguer 2

            //Source--- Referente a las posiciones de los dedos cuando se inicia el escalado
            //Dest----- Referente a las posiciones de los dedos cuando se finaliza el escalado

            //SourcePoints[]: puntos usados para guardar temporalmente los puntos que
            //se van a usar como origen del escalado. (Surgidos ante la duda de
            //si es mejor usar los puntos originales del evento ME.TouchDown
            //o usar los puntos del último cicle ME.TouchMOVE)

            SourcePoints[0].x=dualDrag.OnStarScaling_X[0];
            SourcePoints[0].y=dualDrag.OnStarScaling_Y[0];
            SourcePoints[1].x=dualDrag.OnStarScaling_X[1];
            SourcePoints[1].y=dualDrag.OnStarScaling_Y[1];

            float incX=(dualDrag.TouchX[1]-SourcePoints[1].x)-(dualDrag.TouchX[0]-SourcePoints[0].x);
            float incY=(dualDrag.TouchY[1]-SourcePoints[1].y)-(dualDrag.TouchY[0]-SourcePoints[0].y);
            if(incX>incY)
            {
            }

            //Calcular Touch basado en IncX o IncY

            //CalcularCenters
            PointF CenterTouch=CalcularCenters(dualDrag.TouchX[0],dualDrag.TouchY[0],dualDrag.TouchX[1],dualDrag.TouchY[1]);
            PointF CenterLast=CalcularCenters(dualDrag.LastX[0],dualDrag.LastY[0],dualDrag.LastX[1],dualDrag.LastY[1]);
            PointF CenterOnStartScale=CalcularCenters(dualDrag.OnStarScaling_X[0],dualDrag.OnStarScaling_Y[0],dualDrag.OnStarScaling_X[1],dualDrag.OnStarScaling_Y[1]);

            //Convertir Coordenadas Touch a coordenadas Parent
            PointF Touch0=new PointF(dualDrag.TouchX[0],dualDrag.TouchY[0]);
            PointF Touch1=new PointF(dualDrag.TouchX[1],dualDrag.TouchY[1]);
            PointF Last0=new PointF(dualDrag.LastX[0],dualDrag.LastY[0]);
            PointF Last1=new PointF(dualDrag.LastX[1],dualDrag.LastY[1]);
            PointF OnStartScale0=new PointF(dualDrag.OnStarScaling_X[0],dualDrag.OnStarScaling_Y[0]);
            PointF OnStartScale1=new PointF(dualDrag.OnStarScaling_X[1],dualDrag.OnStarScaling_Y[1]);
            PointF Offset=new PointF(view.getX(),view.getY());
            PointF Center= new PointF(view.getWidth()/2,view.getHeight()/2);
            PointF ParentTouch0=ConvertirCoordenadasTouchAParent(Touch0,Offset,Center);
            PointF ParentTouch1=ConvertirCoordenadasTouchAParent(Touch0,Offset,Center);
            //PointF ParentOnStartScale0=ConvertirCoordenadasTouchAParent(OnStartScale0,Offset);

            //Calcular Escala

            if(incX>incY) {
                // CalcularScala(float ComponenteDominante_valorActual,float ComponenteDominante_ValorAnteror,float ScalaAntigua)
                //CalcularScala(ParentTouch0,);
            }
            else
            {
                // CalcularScala(float ComponenteDominante_valorActual,float ComponenteDominante_ValorAnteror,float ScalaAntigua)
                //CalcularScala();

            }
        }

        return true;
    }

    private void DebugGrid(int cell,String s, String s1, String s2) {
        cell=cell*3;
        if((cell+3)>tvData.size())return;
        tvData.get(cell).setText(s);
        tvData.get(cell+1).setText(s1);
        tvData.get(cell+2).setText(s2);
    }

    protected PointF CalcularCenters (float P1x,float P1y,float P2x,float P2y)
    {
        PointF P1,P2;

        P1=new PointF(P1x,P1y);
        P2=new PointF(P2x,P2y);

        return CalcularCenters(P1,P2);
    }
    protected PointF CalcularCenters (PointF P1,PointF P2)
    {
        PointF returnValue;

        returnValue=new PointF();

        returnValue.x=((P1.x-P2.x)/2)+P2.x;
        returnValue.y=((P1.y-P2.y)/2)+P2.y;


        return returnValue;
    }

    /**
     * Convierte las corrdenadas de un evento onTouch sobre un control view,
     * (con origen en la esquina superior izquierda de la App)
     * en coordenadas con origen con origen en la esquina superior izquierda de ese control "view"
     * @param P_Touch Punto a convertir
     * @param Offset Coordenadas del control View, (devueltas por GetX() y GetY()
     *               mas el desfase producido por la jerarquia de controles
     * @param Center Centro del control view, on origen en su propia esquina superior izquierda
     * @return devuelve el punto del control view que el usuario ha tocado
     */
    protected PointF ConvertirCoordenadasTouchAParent(PointF P_Touch,PointF Offset,PointF Center)
    {
        PointF ScaledPoint=new PointF();

        ScaledPoint=Resta(P_Touch,Suma(Offset,Center));

        return ScaledPoint;
    }
    protected float CalcularScala(float ComponenteDominante_valorActual,float ComponenteDominante_ValorAnteror,float ScalaAntigua)
    {
        float ScaleNueva=ComponenteDominante_valorActual*ScalaAntigua/ComponenteDominante_ValorAnteror;

        return ScaleNueva;
    }
    protected PointF Resta (PointF P1,PointF P2)
    {
        return new PointF(P1.x-P2.x,P1.y-P2.y);
    }
    protected PointF Suma (PointF P1,PointF P2)
    {
        return new PointF (P1.x+P2.x,P1.y+P2.y);
    }
}
