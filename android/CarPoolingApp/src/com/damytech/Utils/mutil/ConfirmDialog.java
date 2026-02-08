package com.damytech.Utils.mutil;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.*;
import com.damytech.PincheApp.R;

public class ConfirmDialog {
    /**默认的对话框视图*/
    public static int DIALOG_UI = R.layout.confirmdialog;

    /**默认的对话框占主屏幕多宽度的比例*/
    public static float WIDTH_SCALE = 0.8F;

    /**OK按钮被点击*/
    public static final int OK = 0;
    /**取消按钮点击*/
    public static final int CANNEL = 1;

    protected Context context;
    protected Dialog dialog;
    protected Button okBtn;
    protected Button cannelBtn;

//    protected int id;
    protected String title;
    protected String message;
    protected boolean flag;
    protected int totalprice;
    private EditText etPrice;
    SharedPreferences sharedPreferences;
    private int addPrice;

    protected ConfirmListener listener;
    protected ConfirmCancelListener cancelListener;

    public ConfirmDialog(Context context){
        this.context = context;
    }
    public ConfirmDialog(Context context, String t, String m){
        this(context);
        this.title = t;
        this.message = m;
    }

    public void setTitle(String t){
        this.title = t;
    }
    public void setMessage(String m){
        this.message = m;
    }
    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public void setConfirmListener(ConfirmListener listener){
        this.listener = listener;
    }
    public void setConfirmCancelListener(ConfirmCancelListener cancelListener){
        this.cancelListener = cancelListener;
    }
    public int getPrice(){
        int price = 0;
        String priceStr = etPrice.getText().toString().trim();
        if(priceStr == null || "".equals(priceStr))
            priceStr = "0";
        price = Integer.parseInt(priceStr);
        return price;
    }

    protected void createDialog(){
        View view = View.inflate(context, getMainXML(), null);
        sharedPreferences = context.getSharedPreferences("wait_time_list", Context.MODE_PRIVATE);
        addPrice = sharedPreferences.getInt("price_add",5);
        ((TextView)view.findViewById(R.id.tv_confirmdialog_title)).setText(title);
        etPrice = (EditText)view.findViewById(R.id.et_price);
        //如果message为null，不显示
        TextView messageTV = (TextView)view.findViewById(R.id.tv_confirmdialog_message);
        if(message == null)
            ((LinearLayout)view).removeView(messageTV);
        else
            messageTV.setText(message);
        dialog = new Dialog(context);
        dialog.show();

        Window win = dialog.getWindow();
        //将dialog的背景透明化
        win.setBackgroundDrawable(new ColorDrawable(0));
        win.setGravity(getGravity());


//		LinearLayout ll = (LinearLayout)view.findViewById(R.id.dialog_live);
//		View liveView = getLiveView();
//		if(liveView != null){
//			ll.addView(liveView);
//		}
        win.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        win.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (dm.widthPixels * WIDTH_SCALE);
        win.setAttributes(lp);
        win.setContentView(view);

        initPrice(view);
        initButton(view);

        etPrice.setText(addPrice+"");
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(charSequence != null && !"".equals(charSequence.toString().trim())
                        &&!"0".equals(charSequence.toString().trim())){
                    okBtn.setEnabled(true);
                    okBtn.setBackgroundResource(R.drawable.roundsolidgreen_frame);
                }else{
                    okBtn.setEnabled(false);
                    okBtn.setBackgroundResource(R.drawable.btn_white_sel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void initPrice(View view){
        ImageView ivAdd = (ImageView)view.findViewById(R.id.iv_upprice);
        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceStr = etPrice.getText().toString().trim();
                if(priceStr == null || "".equals(priceStr))
                    priceStr = "0";

                int price = Integer.parseInt(priceStr) + addPrice;
                etPrice.setText(price+"");
            }
        });
    }

    public void show(){
        if(dialog == null)
            createDialog();
        else
            dialog.show();
    }

    public void cancel(){
        if(dialog != null)
            dialog.dismiss();
    }

    /**
     * @方法名称 :initButton
     * @功能描述 :初始化按钮
     *
     * @param view
     * @return :void
     */
    protected void initButton(View view){
        if(isButtonShow()){
            okBtn = (Button)view.findViewById(R.id.bt_confirmdialog_ok);
            cannelBtn = (Button)view.findViewById(R.id.bt_confirmdialog_cannel);
            okBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    afterClickOK();
                }
            });

            cannelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    afterClickCancel();
                    dialog.dismiss();
                }
            });
        }else{
            View v = view.findViewById(R.id.ll_confirmdialog_button_group);
            ((LinearLayout)view).removeView(v);
        }
    }

    /**
     * @方法名称 :isButtonShow
     * @功能描述 :如果子类不需要显示按钮，可以重写这个方法。
     * @return
     * @return :boolean
     */
    protected boolean isButtonShow(){
        return true;
    }

    /**
     * @方法名称 :getMainXML
     * @功能描述 :获得主视图id
     * @return
     * @return :int
     */
    public int getMainXML(){
        return DIALOG_UI;
    }

    public int getGravity(){
        return Gravity.CENTER;
    }

    /**
     * @方法名称 :afterClickOK
     * @功能描述 :确认按钮点击后触发，子类可以重写这个方法达到不同的效果
     * @return :void
     */
    public void afterClickOK(){
        if(listener != null)
            listener.onConfirmClick(OK, null);
    }
    public void afterClickCancel(){
        if(cancelListener != null)
            cancelListener.onConfirmClick(CANNEL, null);
    }
    /**
     * @方法名称 :getLiveView
     * @功能描述 :得到一个扩展的视图，可以产生不同组合的对话框，子类可以重写这个方法
     * @return
     * @return :View
     */
    public View getLiveView(){
        return null;
    }

    public interface ConfirmListener{

        /**
         * @方法名称 :onConfirmClick
         * @功能描述 :当confirm对话框中的按钮被点击时
         * @param position
         * @return :void
         */
        public void onConfirmClick(int position, Object obj);
    }

    public interface ConfirmCancelListener{

        /**
         * @方法名称 :onConfirmClick
         * @功能描述 :当confirm对话框中的按钮被点击时
         * @param position
         * @return :void
         */
        public void onConfirmClick(int position, Object obj);
    }
}
