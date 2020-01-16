package com.kpcode4u.prasanthkumarkatta.proofofconcept.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kpcode4u.prasanthkumarkatta.proofofconcept.R;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.Country;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryInfo> {
    private Context mContext;
    private List<Country> countryList;

    public CountryAdapter(Context mContext, List<Country> countryList) {
        this.mContext = mContext;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public CountryInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.country_row, parent, false);
        return new CountryInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryInfo holder, int position) {
        Country country = countryList.get(position);
        String title = country.getTitle();
        if (title != null) {
            holder.title.setText(title);
        }
        String description = country.getDescription();
        if (description != null) {
            holder.description.setText(description);
        }

        //Todo: Image loading using Picasso
        String imgUrl = country.getImageHref();
        if (imgUrl != null) {
            Picasso.get()
                    .load(imgUrl)
                    .centerInside()
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        if (countryList.size() != 0) {
            return countryList.size();
        } else {
            Toast.makeText(mContext, "No items in List", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    class CountryInfo extends RecyclerView.ViewHolder {
        // @BindView(R.id.titleTv_row)
        TextView title;
        // @BindView(R.id.descriptionTv_row)
        TextView description;
        // @BindView(R.id.imageView_row)
        ImageView imageView;

        CountryInfo(@NonNull View itemView) {
            super(itemView);
            //ButterKnife.bind(mContext, itemView);
            title = itemView.findViewById(R.id.titleTv_row);
            description = itemView.findViewById(R.id.descriptionTv_row);
            imageView = itemView.findViewById(R.id.imageView_row);

        }
    }
}
