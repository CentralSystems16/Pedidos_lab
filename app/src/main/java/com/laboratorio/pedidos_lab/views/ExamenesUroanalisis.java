package com.laboratorio.pedidos_lab.views;/*public class ExamenesUroanalisis extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView rvlista;
    private RecyclerAdapter adapter;
    private List<ItemList> items;
    private SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examenes_uroanalisis);

        initViews();
        initValues();
        initListener();
    }

    private void initViews(){
        rvlista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);
    }

    private void initValues(){
        GridLayoutManager manager = new GridLayoutManager(this, 3 );
        rvlista.setLayoutManager(manager);

        items = getItems();
        adapter = new RecyclerAdapter(items);
        rvlista.setAdapter(adapter);
    }

    private void initListener(){
        svSearch.setOnQueryTextListener(this);
    }

    private List<ItemList> getItems() {
        List<ItemList> itemLists = new ArrayList<>();
        itemLists.add(new ItemList("CREATININA EN ORINA"," $6.00", R.drawable.mas,203));
        itemLists.add(new ItemList("DEPURACION DE CREATININA DE 24 HRS"," $15.00", R.drawable.mas,204));
        itemLists.add(new ItemList("GENERAL DE ORINA"," $1.75", R.drawable.mas,205));
        itemLists.add(new ItemList("MICROALBUMINA"," $7.00", R.drawable.mas,206));
        itemLists.add(new ItemList("MICROALBUMINA EN ORINA"," $7.00", R.drawable.mas,207));
        itemLists.add(new ItemList("PROTEINAS EN ORINA 24 H"," $18.00", R.drawable.mas,208));
        itemLists.add(new ItemList("PROTEINAS EN ORINA AL AZAR"," $3.00", R.drawable.mas,209));
        itemLists.add(new ItemList("PRUEBA DE EMBARAZO EN ORINA"," $4.00", R.drawable.mas,210)); //ACTIVIDAD
        return itemLists;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    } //Comentario agregado
*/
