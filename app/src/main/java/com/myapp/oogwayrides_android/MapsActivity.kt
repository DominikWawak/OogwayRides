package com.myapp.oogwayrides_android


import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.myapp.oogwayrides_android.controllers.FirebaseController
import com.myapp.oogwayrides_android.controllers.db
import com.myapp.oogwayrides_android.databinding.ActivityMapsBinding
import com.myapp.oogwayrides_android.models.Adventure
import java.util.*


private const val KEY_CAMERA_POSITION = "camera_position"
private const val KEY_LOCATION = "location"



val firebaseController=FirebaseController()

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var addAdvLayout: LinearLayout
    private lateinit var viewAdvLayout: LinearLayout
    private lateinit var mainButton:ImageView

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient
    private lateinit var sideMenu:LinearLayout
    private lateinit var menuOutBtn:ImageView
    private lateinit var myTripsBtn:ImageView
    private lateinit var myFollowersBtn:ImageView
    private lateinit var myAccountBtn:ImageView
    private lateinit var touchMarker:Marker
    private lateinit var datePicker:DatePicker
    private lateinit var datePickerButton: ImageButton
    private lateinit var advName:EditText
    private lateinit var date:TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var plan:EditText
    private lateinit var groupSize:EditText
    private lateinit var locationOfAdventure:ArrayList<String>
    private lateinit var currentUser:String
    private lateinit var advNameBox:TextView
    private lateinit var advVehBox:TextView
    private lateinit var advDateBox:TextView
    private lateinit var advPlanBox:TextView
    private lateinit var deleteAdv:Button
    private lateinit var joinAdvBtn:Button
    private lateinit var selectedAdventureId:String
    private lateinit var selectedMarker:Marker
    private lateinit var searchBar:EditText






    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Construct a PlacesClient
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient  = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationOfAdventure= arrayListOf()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val linearLayout = findViewById<LinearLayout>(R.id.design_bottom_sheet)
        val infoLayout = findViewById<LinearLayout>(R.id.view_adv)
        var vi: View = LayoutInflater.from(this).inflate(R.layout.layout_inflate_info_window, null)
        datePickerButton= findViewById(R.id.datePickerBtn)
        addAdvLayout=findViewById(R.id.add_adv)
        viewAdvLayout= findViewById(R.id.advview)
        menuOutBtn = findViewById<ImageView>(R.id.openSideMenu)
         sideMenu = findViewById<LinearLayout>(R.id.sideMenu)
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
        myFollowersBtn=findViewById(R.id.myFollowersBtn)
        mainButton = findViewById<ImageView>(R.id.mainButton)
        myTripsBtn=findViewById(R.id.myAdvBtn)
        myAccountBtn=findViewById(R.id.myAccountBtn)
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN

        advName= findViewById(R.id.adv_name)
        date=findViewById(R.id.adv_date)
        radioGroup=findViewById(R.id.transportGroup)
        plan=findViewById(R.id.plan)
        groupSize=findViewById(R.id.groupSize)
        deleteAdv=findViewById(R.id.delete_adv_btn)
        joinAdvBtn=findViewById(R.id.advJoinBtn)
        searchBar=findViewById(R.id.searchT)

        val myIntent = intent

        currentUser = myIntent.getStringExtra("currentUser").toString()


       bottomSheetBehavior.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if(newState==BottomSheetBehavior.STATE_HIDDEN){
                       mainButton.visibility=View.VISIBLE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        searchBar.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.d("ENTER", "pressed ")
                val geocoder = Geocoder(this, Locale.getDefault())

                var addresses: MutableList<Address?>? = geocoder.getFromLocationName(searchBar.text.toString(), 1)

                if (addresses != null) {
                    if(addresses.size>0){
                        val lat = addresses[0]!!.latitude
                        val lon = addresses[0]!!.longitude

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 15F));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null);

                    }
                }

                return@OnKeyListener true
            }
            false
        })

        menuOutBtn.setOnClickListener{
            sideMenu.visibility=View.VISIBLE
            menuOutBtn.visibility=View.INVISIBLE
        }


        myTripsBtn.setOnClickListener{
            val intent = Intent(this@MapsActivity, TripsActivity::class.java)
            startActivity(intent)
        }
        myFollowersBtn.setOnClickListener{
            val intent = Intent(this@MapsActivity, FollowersActivity::class.java)
            startActivity(intent)
        }
        myAccountBtn.setOnClickListener{
            val docRef = db.collection("users").document(currentUser)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                        Log.d(TAG, "getUser: "+document.data)
                        val intent = Intent(this@MapsActivity, AccountActivity::class.java)
                        intent.putExtra("userName", document.data?.get("name") as String)
                        intent.putExtra("userEmail", document.data?.get("email") as String)
                        intent.putExtra("userPic", document.data?.get("profilePic") as String)
                        startActivity(intent)
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }


        }
        mainButton.setOnClickListener{

        }
        mainButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->{
                        mainButton.setImageResource(R.drawable.createadvbutton)
                        getDeviceLocation()
                    }
                    MotionEvent.ACTION_UP ->{
                        mainButton.setImageResource(R.drawable.advbtn2)
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        mMap?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.setInfoWindowAdapter(MyInfoWindowAdapter(this))
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro))
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        mMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            locationOfAdventure.clear()
            Log.d("TOUCH", "onMapTouch: ")
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN

            if(this::touchMarker.isInitialized){
                touchMarker.remove()
            }

            if(sideMenu.visibility==View.INVISIBLE) {
                touchMarker =
                    mMap.addMarker(MarkerOptions().position(it).title("Create Adventure"))!!


                locationOfAdventure+=it.latitude.toString()
                locationOfAdventure+=it.longitude.toString()
                viewAdvLayout.visibility=View.INVISIBLE
                addAdvLayout.visibility=View.VISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                mainButton.visibility = View.INVISIBLE
            }

            if(sideMenu.visibility==View.VISIBLE){
                sideMenu.visibility=View.INVISIBLE
                menuOutBtn.visibility=View.VISIBLE

            }
            //mainButton.visibility=View.VISIBLE

        })

        mMap.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            Log.d("marker", "marker clicked" + it.title)
            selectedMarker=it

            if (it.title.equals("Create Adventure")) {
                it.hideInfoWindow()
            } else {
                it.showInfoWindow()
                selectedAdventureId = it.title?.split(",")?.get(4).toString()
            }

            if ((this::touchMarker.isInitialized && it != touchMarker) || !this::touchMarker.isInitialized) {
                addAdvLayout.visibility = View.INVISIBLE
                viewAdvLayout.visibility = View.VISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                mainButton.visibility = View.INVISIBLE

            } else {
                viewAdvLayout.visibility = View.INVISIBLE
                addAdvLayout.visibility = View.VISIBLE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                mainButton.visibility = View.INVISIBLE
            }

        if(this::selectedAdventureId.isInitialized){
            val docRef = db.collection("adventures").document(selectedAdventureId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        if (document.data?.get("organizer").toString() == currentUser) {
                            deleteAdv.visibility = View.VISIBLE
                            //TODO notfy with already going
                            joinAdvBtn.visibility=View.INVISIBLE

                        } else {
                            deleteAdv.visibility = View.INVISIBLE
                            joinAdvBtn.visibility=View.VISIBLE
                        }
                        // arrayOf(doc.data["passangers"]).joinToString(" ").contains(
                        if(arrayOf(document.data?.get("passangers")).joinToString(" ").contains(currentUser)){
                            joinAdvBtn.visibility=View.INVISIBLE
                        }

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
            return@OnMarkerClickListener true
        })
        //load
        loadMarkers()

    }


    /**
     * Load custom adventure markers onto the map
     */
    fun loadMarkers(){
        db.collection("adventures")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Result: $result")
                for (document in result) {

                    Log.d("locloc", "location: "+ document.data["location"] as ArrayList<String>)
                    var coordinates = document.data["location"] as ArrayList<String>

                    mMap?.addMarker(
                        MarkerOptions()
                            .position(LatLng(coordinates[0].toDouble(), coordinates[1].toDouble()))
                            .title(document.data["name"].toString()+","+document.data["date"].toString()+","+document.data["plan"].toString()+","+document.data["vehicle"].toString()+","+document.id)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        //https://stackoverflow.com/questions/14226453/google-maps-api-v2-how-to-make-markers-clickable

    }



    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (mMap == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }


                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            mMap?.addMarker(MarkerOptions()
                .title("Sydney")
                .position(defaultLocation)
                .snippet("info Snippet"))


            // Prompt the user for permission.
            getLocationPermission()
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = false //hide camera

            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }




    fun pickDate(view:View){
        val calendar = Calendar.getInstance()
        val currYear = calendar.get(Calendar.YEAR)
        val currMonth = calendar.get(Calendar.MONTH)
        val currDay= calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker=DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view,year,month,dayOfMonth->
            date.text=""+dayOfMonth+"/"+(month+1)+"/"+year
        },currYear,currMonth,currDay)

        datePicker.show()

    }


    fun joinAdventure(view: View){
        Log.d("JOIN", "joinAdventure: ")
        firebaseController.joinAdv(currentUser,selectedAdventureId)
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        var toast=Toast.makeText(applicationContext, "🗺 JOINED Adventure", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()

    }
    fun deleteAdventure(view: View){
        Log.d("Delete", "deleteAdv")
        firebaseController.deleteAdv(currentUser,selectedAdventureId)
        selectedMarker.remove()
    }

    fun createAdventure(view: View){

        if(!advName.text.equals("")) {
                firebaseController.addAdventure(Adventure(currentUser,locationOfAdventure,null,advName.text.toString(),findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString(),date.text.toString(),groupSize.text.toString().toInt(),plan.text.toString()))

                bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
                var toast=Toast.makeText(applicationContext, "✅ Adventure Created", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL , 0, 0)
                toast.show()

                advName.text.clear()
                date.text=""
                radioGroup.clearCheck()
                plan.text.clear()
                groupSize.text.clear()


                loadMarkers()
            }

    }




    companion object {


        private val TAG = MapsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5

    }


    /**
     * This is the class used to style window that appears when the marker is clicked.
     */
    class MyInfoWindowAdapter(mContext: Context) : GoogleMap.InfoWindowAdapter {
        var mWindow: View = LayoutInflater.from(mContext).inflate(R.layout.layout_inflate_info_window, null)


        private fun setInfoWindowText(marker: Marker) {

            val title = marker.title?.split(",")?.get(0) ?: "none"


            val advDateBox=mWindow.findViewById<TextView>(R.id.dateBox)
            val  advPlanBox=mWindow.findViewById<TextView>(R.id.planBox)
            val advVehBox=mWindow.findViewById<TextView>(R.id.vehBox)


            val tvTitle = mWindow.findViewById<TextView>(R.id.nameBox)
            if (!TextUtils.isEmpty(title)) {
                tvTitle.text = title
                advDateBox.text=marker.title?.split(",")?.get(1) ?: "none"
                advPlanBox.text=marker.title?.split(",")?.get(2) ?: "none"
                advVehBox.text=marker.title?.split(",")?.get(3) ?: "none"
            }
        }

        override fun getInfoWindow(p0: Marker): View {
            setInfoWindowText(p0)
            return mWindow
        }

        override fun getInfoContents(p0: Marker): View {
            setInfoWindowText(p0)
            return mWindow
        }
    }


}
