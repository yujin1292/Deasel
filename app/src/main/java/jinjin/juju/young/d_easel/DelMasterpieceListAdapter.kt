package jinjin.juju.young.d_easel

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class DelMasterpieceListAdapter (realmResults: OrderedRealmCollection<ImageDB>): RealmBaseAdapter<ImageDB>(realmResults){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh:DViewHolder
        val view: View

        if(convertView==null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.checkmasterpiece,parent,false)
            vh = DViewHolder(view)
            view.tag = vh
        }else{
            view = convertView
            vh = view.tag  as DViewHolder
        }
        if(adapterData!=null){
            val item = adapterData!![position]
            val arr = item.image
            var image = BitmapFactory.decodeByteArray(arr, 0, arr.size)
            vh.piece.setImageBitmap(image)
        }
        return view
    }
}


class DViewHolder(view: View) {
    val piece: ImageView = view.findViewById(R.id.delmasterpieceImage)
}