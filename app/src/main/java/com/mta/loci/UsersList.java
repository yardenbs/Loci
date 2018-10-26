package com.mta.loci;

public class UsersList {

    public String name, image;

    public UsersList() {
    }

    public UsersList(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
//    private Activity context;
//    private ArrayList<LociUser> usersList;
//    public UsersList(Activity context, ArrayList<LociUser> usersList){
//        super(context, R.layout.activity_search, usersList );
//        this.context = context;
//        this.usersList =usersList;
//
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.activity_search, null, true);
//        TextView textViewName =(TextView) listViewItem.findViewById(R.id.textViewName);
//        LociUser user = usersList.get(position);
//        textViewName.setText(user.getName());
//        return  listViewItem;
//    }
}
