package com.example.user.zoom3_asus;

import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

public class DualDrag {

    public float[] OnStarScaling_X,OnStarScaling_Y;
    public float[] LastX,LastY;
    public float[] TouchX,TouchY;
    public int[] ID;
    public int pointerCount=0;
    public int action=0;
    public boolean moving=false;
    public boolean scaling=false;
    public boolean StartScaling=false;
    public int LastFinger=0;

    //Constructor recomendado.
    public DualDrag (){IniArray();}
    //Constructor "inline", para ser llamado cuando se dispone de un Objeto MotionEvent
    //servido por una interfaz OnTouch(). (no recomendado)
    public DualDrag (MotionEvent ME)
    {
        int local_pointerCount;
        IniArray();
        local_pointerCount=ME.getPointerCount();
        if(local_pointerCount>2)
        {
            //Hay mas de dos dedos en pantalla
            //es interpretado como una cancelacion del gesto
            moving=false;
            scaling=false;
            return;
        }
        //Al ser una inicializacion de clase,
        //simplemente guarda los dos dedos en pantalla
        for(int index=0;index<local_pointerCount;index++)
        {
            LastX[index]=ME.getX(index);
            LastY[index]=ME.getY(index);
            TouchX[index]=ME.getX(index);
            TouchY[index]=ME.getY(index);
            ID[index]=ME.getPointerId(index);
        }
        switch (local_pointerCount)
        {
            case 0:
                //No hay dedos en pantalla, sin interes
                break;
            case 1:
                //Inicio de movimiento
                moving=true;
                scaling=false;
                break;
            case 2:
                //Inicio de escalado
                moving=false;
                scaling=true;
                StartScaling=true;
                break;
        }
    }

    /**
     * FirstTouch: Tramita las operaciones necesarias cuando el usuatio realiza el
     * primer Touch en pantalla.
     * @param ME
     */
    protected void FirstTouch (MotionEvent ME)
    {
        if (ME.getPointerCount()==0)return;
        pointerCount=1;
        moving=true;
        scaling=false;
        int index=ME.getActionIndex();
        LastX[0]=ME.getX(index);
        LastY[0]=ME.getY(index);
        TouchX[0]=ME.getX(index);
        TouchY[0]=ME.getY(index);
        OnStarScaling_X[0]= ME.getX(index);
        OnStarScaling_Y[0]= ME.getY(index);
        ID[0]=ME.getPointerId(index);

    }
    /**
     * SecondTouch: Tramita las operaciones necesarias cuando el usuatio realiza el
     * segundo Touch en pantalla.
     * @param ME
     */
    protected void SecondTouch (MotionEvent ME)
    {
        int local_ID;
        int local_index;

        if (ME.getPointerCount()<=1)return;

        local_index=ME.getActionIndex();
        local_ID=ME.getPointerId(local_index);

        if (local_ID != ID[0]) {
            //Segundo touch, empieza el escalado, cambia el modo de movimiento.
            moving=false;
            scaling=true;
            StartScaling=true;
            pointerCount=2;
            LastX[1]=ME.getX(local_index);
            LastY[1]=ME.getY(local_index);
            TouchX[1]=ME.getX(local_index);
            TouchY[1]=ME.getY(local_index);
            ID[1]=local_ID;
        }

    }

    /**
     * MotionEventFilter; recive en ME el último evento Touch realizado por el usuario en Pantalla
     * y rellena con esa información las variables internas útiles para realizar un
     * desplazamiento/escalado.
     * @param ME
     * @param ValidRegion
     */
    public void MovionEventFilter (MotionEvent ME, Rect ValidRegion)
    {
        int local_pointerCount;
        int local_ID;
        int local_action;
        int local_index;

        local_action=ME.getActionMasked();
        local_index=ME.getActionIndex();
        local_ID=ME.getPointerId(local_index);
        local_pointerCount=ME.getPointerCount();

        if(local_pointerCount>2)
        {
            //Hay mas de dos dedos en pantalla
            //es interpretado como una cancelacion del gesto
            moving=false;
            scaling=false;
            StartScaling=false;
            return;
        }

        switch (local_action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(ValidRegion.contains((int)ME.getX(local_index),(int)ME.getY(local_index))) {
                    switch (pointerCount) {
                        case 0:
                            //Primer touch, empieza el movimiento.
                            FirstTouch(ME);
                            break;
                        case 1:
                            if (local_ID != ID[0]) {
                                //Segundo touch, empieza el escalado, cambia el modo de movimiento.
                                SecondTouch(ME);
                            }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (pointerCount) {
                    case 0:
                        //Error... un evento ACTION_DOWN se ha perdido o ha sido
                        //efectuadi fuera de ValidRegion
                        //EL ACTION_MOVE actuel se ignora,
                        break;
                    case 1:
                        //Continua el movimiento
                        if (local_ID == ID[0]) {
                            LastX[0] = TouchX[0];
                            LastY[0] = TouchY[0];
                            local_index = ME.findPointerIndex(ID[0]);
                            if (local_index != -1) {
                                TouchX[0] = ME.getX(local_index);
                                TouchY[0] = ME.getY(local_index);
                            }
                            break;
                        }
                        break;
                    case 2:
                        //Continua el escalado y el movimiento alternativo

                        LastX[0] = TouchX[0];
                        LastY[0] = TouchY[0];
                        local_index = ME.findPointerIndex(ID[0]);
                        if (local_index != -1) {
                            TouchX[0] = ME.getX(ME.findPointerIndex(ID[0]));
                            TouchY[0] = ME.getY(ME.findPointerIndex(ID[0]));
                        }
                        LastX[1] = TouchX[1];
                        LastY[1] = TouchY[1];
                        local_index = ME.findPointerIndex(ID[1]);
                        if (local_index != -1) {
                            TouchX[1] = ME.getX(ME.findPointerIndex(ID[1]));
                            TouchY[1] = ME.getY(ME.findPointerIndex(ID[1]));
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                switch (pointerCount)
                {
                    case 0:
                        //Error. Se ha perdido un evento ACTION_DOWN
                        //o se ha efectuado fuera de ValidRegion
                        //Se interpreta como
                        //.... ¿que coño pongo aquí? ....
                        break;
                    case 1:
                        //Comprueba que el dedo levantado sea el que está controlando el movimiento
                        if(local_ID==ID[0]) {
                            //fin del movimiento.
                            moving = false;
                            //un ACTION_DOWN podría haberse perdido,
                            //así que se cancela el escalado por si acaso
                            scaling = false;
                            StartScaling=false;
                            //ya no hay dedos en pantalla
                            pointerCount = 0;
                        }
                        break;
                    case 2:
                        //Comprobamos si el dedo levantado es el uno o el dos
                        if(local_ID==ID[0]) {
                            //cambia el puntero 1 por el puntero cero
                            LastX[0] = LastX[1];
                            LastY[0] = LastY[1];
                            TouchX[0] = TouchX[1];
                            TouchY[0] = TouchY[1];
                            ID[0]=ID[1];
                            //fin del escalado y del movimiento alternativo.
                            scaling = false;
                            StartScaling=false;
                            //Inicio del movimiento clasico
                            moving = true;
                            //ya solo hay un dedo en pantalla
                            pointerCount = 1;
                        }
                        else
                        {
                            if(local_ID==ID[1])
                            {
                                //fin del escalado y del movimiento alternativo.
                                scaling = false;
                                StartScaling=false;
                                //Inicio del movimiento clasico
                                moving = true;
                                //ya solo hay un dedo en pantalla
                                pointerCount = 1;
                            }
                        }
                        break;
                }
                break;
        }


    }
    protected void IniArray()
    {
        OnStarScaling_X=new float[2];
        OnStarScaling_Y=new float[2];
        LastX=new float[2];
        LastY=new float[2];
        TouchX=new float[2];
        TouchY=new float[2];
        ID=new int[2];
    }

    /**Calcula El desplazamiento y la escala que debe sufrir una imagen con la infoemación
     * de las variables internas de la clase DualDrag
     */
    public void CalculateOffsetAndScacle (PointF ImageCenter,PointF ImageOffset,int ImageScale)
    {
        if(scaling) {
            //NewOffset: Nueva Posicion de la imagen respecto a su Parent,
            PointF NewOffset;
            //NewScale: Nueva Escala de la imagen
            float NewScale = 0;
            //PointLast: vector desde el centro de la imagen al punto guardado como "Last"
            //PointTouch:Vector desde el centro de la imagen al punto guardado como "Touch"
            //CenterLast: punto central entre los dos puntos "Last"
            //CanterTouch: punto central entre los dos puntos "Touch"
            PointF[] PointLast, PointTouch;
            PointF CenterLast, CenterTouch;

            PointLast = new PointF[2];
            PointTouch = new PointF[2];
            PointLast[0] = new PointF(LastX[0], LastY[0]);
            PointTouch[0] = new PointF(TouchX[0], TouchY[0]);
            PointLast[1] = new PointF(LastX[1], LastY[1]);
            PointTouch[1] = new PointF(TouchX[1], TouchY[1]);

            CenterLast = Center(PointLast[0], PointLast[1]);
            CenterTouch = Center(PointTouch[0], PointTouch[1]);
            NewOffset = new PointF();
        }

    }
    public PointF Center (PointF Point1,PointF Point2)
    {
        return new PointF(
                Point1.x-((Point1.x-Point2.x)/2),
                Point1.y-((Point1.y-Point2.y)/2)
        );
    }

    public PointF ParentCoords_To_CenterCoords (PointF SourcePoint,PointF PictureOffset,PointF Dimensions)
    {
        PointF DestPoint;

        DestPoint=new PointF();

        return DestPoint;
    }
}
