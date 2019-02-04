package com.ats.easyrto.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.BuyProduct;
import com.ats.easyrto.activity.ImageZoomActivity;
import com.ats.easyrto.model.WorkHeader;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WorkStatusAdapter extends RecyclerView.Adapter<WorkStatusAdapter.MyViewHolder> {

    private ArrayList<WorkHeader> workList;
    private Context context;

    public WorkStatusAdapter(ArrayList<WorkHeader> workList, Context context) {
        this.workList = workList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWorkType, tvCost, tvDate, tvAmtPaid, tvAmtRemaining, tvStatus, tvPhoto1, tvPhoto2, tvAadhar, tvRC, tvInsc1, tvInsc2, tvPUC, tvBankLetter, tvForm17, tvBankNOC, tvAddProof, tvOrigLic, tvFIR, tvVehicleInfo;
        public Button btnPayment;

        public MyViewHolder(View view) {
            super(view);
            tvWorkType = view.findViewById(R.id.tvWorkType);
            tvCost = view.findViewById(R.id.tvCost);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmtPaid = view.findViewById(R.id.tvAmtPaid);
            tvAmtRemaining = view.findViewById(R.id.tvAmtRemaining);
            tvStatus = view.findViewById(R.id.tvStatus);

            tvPhoto1 = view.findViewById(R.id.tvPhoto1);
            tvPhoto2 = view.findViewById(R.id.tvPhoto2);
            tvAadhar = view.findViewById(R.id.tvAadhar);
            tvRC = view.findViewById(R.id.tvRC);
            tvInsc1 = view.findViewById(R.id.tvInsc1);
            tvInsc2 = view.findViewById(R.id.tvInsc2);
            tvPUC = view.findViewById(R.id.tvPUC);
            tvBankLetter = view.findViewById(R.id.tvBankLetter);
            tvForm17 = view.findViewById(R.id.tvForm17);
            tvBankNOC = view.findViewById(R.id.tvBankNOC);
            tvAddProof = view.findViewById(R.id.tvAddProof);
            tvOrigLic = view.findViewById(R.id.tvOrigLic);
            tvFIR = view.findViewById(R.id.tvFIR);
            tvVehicleInfo = view.findViewById(R.id.tvVehicleInfo);

            btnPayment = view.findViewById(R.id.btnPayment);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_work_status, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final WorkHeader model = workList.get(position);

        holder.tvWorkType.setText("" + model.getWorkTypeName());
        holder.tvDate.setText("" + model.getDate1());
        holder.tvCost.setText("" + model.getWorkCost() + "/-");
        holder.tvAmtRemaining.setText("" + model.getExInt2() + "/-");
        holder.tvAmtPaid.setText("" + model.getExInt1() + "/-");

        String status = "";
        if (model.getStatus() == 1) {
            status = "Upload Documents";
        } else if (model.getStatus() == 2) {
            status = "Update Work Cost";
            //holder.btnPayment.setVisibility(View.VISIBLE);
        } else if (model.getStatus() == 3) {
            status = "Update Payment Done";
        } else if (model.getStatus() == 4) {
            status = "User Allocation";
        } else if (model.getStatus() == 5) {
            status = "Document In Office";
        } else if (model.getStatus() == 6) {
            status = "Document Submit to RTO";
        } else if (model.getStatus() == 7) {
            status = "Handover To Customer";
        }
        holder.tvStatus.setText("" + status);


        if (model.getExInt2()>0){
            holder.btnPayment.setVisibility(View.VISIBLE);
        }else{
            holder.btnPayment.setVisibility(View.GONE);
        }

        if (model.getPhoto1() != null) {
            if (!model.getPhoto1().isEmpty()) {
                holder.tvPhoto2.setVisibility(View.VISIBLE);
            } else {
                holder.tvPhoto2.setVisibility(View.GONE);
            }
        }

        if (model.getPhoto() != null) {
            if (!model.getPhoto().isEmpty()) {
                holder.tvPhoto1.setVisibility(View.VISIBLE);
            } else {
                holder.tvPhoto1.setVisibility(View.GONE);
            }
        }

        if (model.getAdharCard() != null) {
            if (!model.getAdharCard().isEmpty()) {
                holder.tvAadhar.setVisibility(View.VISIBLE);
            } else {
                holder.tvAadhar.setVisibility(View.GONE);
            }
        }

        if (model.getRcbook() != null) {
            if (!model.getRcbook().isEmpty()) {
                holder.tvRC.setVisibility(View.VISIBLE);
            } else {
                holder.tvRC.setVisibility(View.GONE);
            }
        }

        if (model.getInsurance() != null) {
            if (!model.getInsurance().isEmpty()) {
                holder.tvInsc1.setVisibility(View.VISIBLE);
            } else {
                holder.tvInsc1.setVisibility(View.GONE);
            }
        }

        if (model.getInsurance1() != null) {
            if (!model.getInsurance1().isEmpty()) {
                holder.tvInsc2.setVisibility(View.VISIBLE);
            } else {
                holder.tvInsc2.setVisibility(View.GONE);
            }
        }

        if (model.getPuc() != null) {
            if (!model.getPuc().isEmpty()) {
                holder.tvPUC.setVisibility(View.VISIBLE);
            } else {
                holder.tvPUC.setVisibility(View.GONE);
            }
        }

        if (model.getAddProof() != null) {
            if (!model.getAddProof().isEmpty()) {
                holder.tvAddProof.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddProof.setVisibility(View.GONE);
            }
        }

        if (model.getOrignalLicence() != null) {
            if (!model.getOrignalLicence().isEmpty()) {
                holder.tvOrigLic.setVisibility(View.VISIBLE);
            } else {
                holder.tvOrigLic.setVisibility(View.GONE);
            }
        }

        if (model.getBankDocument() != null) {
            if (!model.getBankDocument().isEmpty()) {
                if (model.getWorkTypeTd() == 6) {
                    holder.tvFIR.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBankLetter.setVisibility(View.VISIBLE);
                }
            } else {
                if (model.getWorkTypeTd() == 6) {
                    holder.tvFIR.setVisibility(View.GONE);
                } else {
                    holder.tvBankLetter.setVisibility(View.GONE);
                }
            }
        }

        if (model.getBankDocument1() != null) {
            if (!model.getBankDocument1().isEmpty()) {
                if (model.getWorkTypeTd() == 6) {
                    holder.tvVehicleInfo.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBankNOC.setVisibility(View.VISIBLE);
                }
            } else {
                if (model.getWorkTypeTd() == 6) {
                    holder.tvVehicleInfo.setVisibility(View.GONE);
                } else {
                    holder.tvBankNOC.setVisibility(View.GONE);
                }
            }
        }

        if (model.getExStr2() != null) {
            if (!model.getExStr2().isEmpty()) {
                holder.tvForm17.setVisibility(View.VISIBLE);
            } else {
                holder.tvForm17.setVisibility(View.GONE);
            }
        }


        /*if (model.getWorkTypeTd() == 1) {
            holder.tvPhoto1.setVisibility(View.VISIBLE);
            holder.tvPhoto2.setVisibility(View.VISIBLE);
            holder.tvAadhar.setVisibility(View.VISIBLE);
            holder.tvRC.setVisibility(View.VISIBLE);
            holder.tvInsc1.setVisibility(View.VISIBLE);
            holder.tvInsc2.setVisibility(View.VISIBLE);
            holder.tvPUC.setVisibility(View.VISIBLE);
            holder.tvAddProof.setVisibility(View.VISIBLE);

        } else if (model.getWorkTypeTd() == 2) {
            holder.tvPhoto1.setVisibility(View.VISIBLE);
            holder.tvPhoto2.setVisibility(View.VISIBLE);
            holder.tvAadhar.setVisibility(View.VISIBLE);
            holder.tvRC.setVisibility(View.VISIBLE);
            holder.tvInsc1.setVisibility(View.VISIBLE);
            holder.tvInsc2.setVisibility(View.VISIBLE);
            holder.tvPUC.setVisibility(View.VISIBLE);
            holder.tvBankNOC.setVisibility(View.VISIBLE);

        } else if (model.getWorkTypeTd() == 3) {
            holder.tvPhoto1.setVisibility(View.VISIBLE);
            holder.tvPhoto2.setVisibility(View.VISIBLE);
            holder.tvAadhar.setVisibility(View.VISIBLE);
            holder.tvRC.setVisibility(View.VISIBLE);
            holder.tvInsc1.setVisibility(View.VISIBLE);
            holder.tvInsc2.setVisibility(View.VISIBLE);
            holder.tvPUC.setVisibility(View.VISIBLE);
            holder.tvBankLetter.setVisibility(View.VISIBLE);
            holder.tvForm17.setVisibility(View.VISIBLE);

        } else if (model.getWorkTypeTd() == 4) {
            holder.tvPhoto1.setVisibility(View.VISIBLE);
            holder.tvPhoto2.setVisibility(View.VISIBLE);
            holder.tvAadhar.setVisibility(View.VISIBLE);
            holder.tvRC.setVisibility(View.VISIBLE);
            holder.tvInsc1.setVisibility(View.VISIBLE);
            holder.tvInsc2.setVisibility(View.VISIBLE);
            holder.tvPUC.setVisibility(View.VISIBLE);
            holder.tvAddProof.setVisibility(View.VISIBLE);

        } else if (model.getWorkTypeTd() == 5) {
            holder.tvPhoto1.setVisibility(View.VISIBLE);
            holder.tvPhoto2.setVisibility(View.VISIBLE);
            holder.tvAadhar.setVisibility(View.VISIBLE);
            holder.tvOrigLic.setVisibility(View.VISIBLE);

        }*/


        holder.tvPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getPhoto());
                context.startActivity(intent);
            }
        });

        holder.tvPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getPhoto1());
                context.startActivity(intent);
            }
        });

        holder.tvAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getAdharCard());
                context.startActivity(intent);
            }
        });

        holder.tvRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getRcbook());
                context.startActivity(intent);
            }
        });

        holder.tvInsc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getInsurance());
                context.startActivity(intent);
            }
        });

        holder.tvInsc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getInsurance1());
                context.startActivity(intent);
            }
        });

        holder.tvPUC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getPuc());
                context.startActivity(intent);
            }
        });

        holder.tvBankLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getBankDocument());
                context.startActivity(intent);
            }
        });

        holder.tvForm17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getExStr2());
                context.startActivity(intent);
            }
        });


        holder.tvBankNOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getBankDocument1());
                context.startActivity(intent);
            }
        });

        holder.tvAddProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getAddProof());
                context.startActivity(intent);
            }
        });

        holder.tvOrigLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getOrignalLicence());
                context.startActivity(intent);
            }
        });

        holder.tvFIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getBankDocument());
                context.startActivity(intent);
            }
        });

        holder.tvVehicleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", "" + model.getBankDocument1());
                context.startActivity(intent);
            }
        });


        holder.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog openDialog = new Dialog(context);
                openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                openDialog.setContentView(R.layout.dialog_cost);

                Window window = openDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.x = 100;
                wlp.y = 100;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                TextView tvTask = openDialog.findViewById(R.id.tvTask);
                final EditText edAmt = openDialog.findViewById(R.id.edAmt);
                Button btnSubmit = openDialog.findViewById(R.id.btnSubmit);
                Button btnCancel = openDialog.findViewById(R.id.btnCancel);

                tvTask.setText("" + model.getWorkTypeName());

                if (model.getExInt1() == 0) {
                    edAmt.setText("" + model.getWorkCost());
                } else if (model.getExInt1() > 0) {
                    edAmt.setText("" + model.getExInt2());
                }

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (edAmt.getText().toString().isEmpty()) {
                            edAmt.setError("required");
                        } else {
                            edAmt.setError(null);

                            float amt = Float.parseFloat(edAmt.getText().toString());

                            float cost = model.getWorkCost();
                            float done = model.getExInt1();
                            float pending = model.getExInt2();

                            if (amt == 0) {
                                edAmt.setError("please enter amount");
                            } else {


                                if (done == 0) {

                                    if (amt > cost) {
                                        edAmt.setError("Amount exceeds");
                                    } else if (amt == cost) {
                                        edAmt.setError(null);

                                        Intent intent = new Intent(context, BuyProduct.class);
                                        intent.putExtra("task", model.getWorkTypeName());
                                        intent.putExtra("amount", amt);
                                        intent.putExtra("id", model.getWorkId());
                                        intent.putExtra("workCost", model.getWorkCost());
                                        intent.putExtra("done", amt);
                                        intent.putExtra("pending", 0);
                                        context.startActivity(intent);

                                    } else if (amt < cost) {
                                        edAmt.setError(null);

                                        float pendingCost = cost - amt;

                                        Intent intent = new Intent(context, BuyProduct.class);
                                        intent.putExtra("task", model.getWorkTypeName());
                                        intent.putExtra("amount", amt);
                                        intent.putExtra("id", model.getWorkId());
                                        intent.putExtra("workCost", model.getWorkCost());
                                        intent.putExtra("done", amt);
                                        intent.putExtra("pending", pendingCost);
                                        context.startActivity(intent);

                                    }

                                } else if (pending > 0) {

                                    if (amt > pending) {
                                        edAmt.setError("Amount exceeds");
                                    } else {
                                        edAmt.setError(null);

                                        if (amt == pending) {
                                            pending = 0;

                                            Intent intent = new Intent(context, BuyProduct.class);
                                            intent.putExtra("task", model.getWorkTypeName());
                                            intent.putExtra("amount", amt);
                                            intent.putExtra("id", model.getWorkId());
                                            intent.putExtra("workCost", model.getWorkCost());
                                            intent.putExtra("done", model.getWorkCost());
                                            intent.putExtra("pending", pending);
                                            context.startActivity(intent);

                                        } else if (amt < pending) {
                                            pending = pending - amt;
                                            done = cost - pending;

                                            Intent intent = new Intent(context, BuyProduct.class);
                                            intent.putExtra("task", model.getWorkTypeName());
                                            intent.putExtra("amount", amt);
                                            intent.putExtra("id", model.getWorkId());
                                            intent.putExtra("workCost", model.getWorkCost());
                                            intent.putExtra("done", done);
                                            intent.putExtra("pending", pending);
                                            context.startActivity(intent);

                                        }

                                    }


                                }
                            }
                        }
                    }
                });
                openDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

}
