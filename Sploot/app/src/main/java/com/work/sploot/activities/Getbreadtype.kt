package com.work.sploot.activities

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.getbreadtype.view.*

class Getbreadtype : Fragment() {
    private var splootDB: SplootAppDB? = null


    companion object {
        var petType=0
        fun newInstance(petTypedata:Int): Getbreadtype {
            petType=petTypedata
            return Getbreadtype()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val views = inflater.inflate(R.layout.getbreadtype, container, false)

        splootDB = SplootAppDB.getInstance(views.context)


        val dog = arrayOf(
            "Afador",
            "Affenpinscher",
            "Afghan hound",
            "Airedale Terrier",
            "Akbash",
            "Akita",
            "Alaskan Klee Kai",
            "Alaskan Malamute",
            "American Bulldog",
            "American English Coonhound",
            "American Eskimo Dog",
            "American Foxhound",
            "American Leopard Hound",
            "American Pit Bull Terrier",
            "American Pugabull",
            "American Staffordshire Terrier",
            "American Water Spaniel",
            "Anatolian Shepherd Dog",
            "Appenzeller Sennenhunde",
            "Auggie",
            "Aussiedoodle",
            "Aussiepom",
            "Australian Cattle Dog",
            "Australian Kelpie",
            "Australian Retriever",
            "Australian Shepherd",
            "Australian Shepherd Husky",
            "Australian Shepherd Lab Mix",
            "Australian Terrier",
            "Azawakh",
            "Barbet",
            "Basenji",
            "Bassador",
            "Basset Fauve de Bretagne",
            "Basset Hound",
            "Basset Retriever",
            "Bavarian Mountain Scent Hound",
            "Beabull",
            "Beagle",
            "Beaglier",
            "Bearded Collie",
            "Bedlington Terrier",
            "Belgian Malinoi",
            "Belgian Sheepdog",
            " Belgian Tervuren",
            "Berger Picard",
            "Bernedoodle",
            " Bernese Mountain Dog",
            "Bichon Frise",
            "Biewer Terrier",
            "Black and Tan Coonhound",
            "Black Mouth Cur",
            "Black Russian Terrier",
            "Bloodhound",
            "Blue Lacy",
            "Bluetick Coonhound",
            "Bocker",
            "Boerboel",
            "Boglen Terrier",
            "Bolognese",
            "Borador",
            "Border Collie",
            "Border Sheepdog",
            "Border Terrier",
            "Bordoodle",
            "Borzoi",
            "BoShih",
            "Bossie",
            "Boston Boxer",
            "Boston Terrier",
            "Bouvier des Flandres",
            "Boxer",
            "Boxerdoodle",
            "Boxmatian",
            "Boxweiler",
            "Boykin Spaniel",
            "Brocco Italiano",
            "Braque de Bourbonnais",
            "Briard",
            "Brittany",
            "Broholmer",
            "Brussels Griffon",
            "Bugg",
            "Bull Terrier",
            "Bullboxer Pit",
            "Bulldog",
            "Bullmastiff",
            "Bullmatian",
            "Cairn Terrier",
            "Canaan Dog",
            "Cane Corso",
            "Cardigan Welsh Corgi",
            "Catahoula Bulldog",
            "Catahoula Leopard Dog",
            "Caucasian Shepherd Dog",
            "Cav-a-Jack",
            "Cavachon",
            "Cavador",
            "Cavalier King Charles Spaniel",
            "Cavapoo",
            "Cesky Terrier",
            "Chabrador",
            "Cheagle",
            "Chesapeake Bay Retriever",
            "Chi-Poo",
            "Chihuahua",
            "Chilier",
            "Chinese Crested",
            "Chinese Shar-Pei",
            "Chinook",
            "Chipin",
            "Chiweenie",
            "Chow Chow",
            " Chug",
            "Chusky",
            "Cirneco dell'Etna",
            "Clumber Spaniel",
            "Cockalier",
            "Cockapoo",
            "Cocker Spaniel",
            "Collie",
            "Corgi Inu",
            "Corgidor",
            "Corman Shepherd",
            "Coton de Tulear",
            "Curly-Coated Retriever",
            "Dachsador",
            "Dachshund",
            " Dalmatian",
            "Dandie Dinmont Terrier",
            "Daniff",
            "Deutscher Wachtelhund",
            "Doberdor",
            "Doberman Pinscher",
            "Docker",
            "Dogo Argentino",
            "Dogue de Bordeaux",
            "Dorgi",
            "Doxiepoo",
            "Doxle",
            "Drentsche",
            "Patrijshond",
            "Drever",
            " Dutch Shepherd",
            " English Cocker Spaniel",
            "English Foxhound",
            " English Setter",
            "English Springer Spaniel",
            "English Toy Spaniel",
            " Entlebucher Mountain Dog",
            "Estrela Mountain Dog",
            "Eurasier",
            "Field Spaniel",
            "Finnish Lapphund",
            "Finnish Spitz",
            "Flat-Coated Retriever",
            "Fox Terrier",
            "French Bulldog",
            "French Spaniel",
            "German Pinscher",
            "German Shepherd Dog",
            "German Sheprador",
            "German Shorthaired Pointer",
            "German Spitz",
            "German Wirehaired Pointer",
            "Giant Schnauzer",
            "Glen of Imaal Terrier",
            "Goberian",
            "Goldador",
            "Golden Cocker Retriever",
            "Golden Mountain Dog",
            " Golden Retriever",
            " Golden Shepherd",
            "Goldendoodle",
            "Gollie",
            "Gordon Setter",
            "Great Dane",
            "Great Pyrenees",
            "Greater Swiss Mountain Dog",
            "Greyhound",
            "Hamiltonstovare",
            "Hanoverian Scenthound",
            "Harrier",
            "Havanese",
            "Hokkaido",
            "Horgi",
            "Huskita",
            "Huskydoodle",
            "Ibizan Hound",
            "Icelandic Sheepdog",
            "Irish Red and White Setter",
            "Irish Setter",
            "Irish Terrier",
            "Irish Water Spaniel",
            "Irish Wolfhound",
            "Italian Greyhound",
            "Jack Chi",
            "Jack Russel Terrier",
            "Japanese Chin",
            "Japanese Spitz",
            "Korean Jindo Dog",
            "Karelian Bear Dog",
            "Keeshond",
            "Kerry Blue Terrier",
            "Komondor",
            "Kooikerhondje",
            "Kuvasz",
            "Kyi-Leo",
            "Lab Pointer",
            "Labernese",
            "Labmaraner",
            "Labrabull",
            "Labradane",
            "Labradoodle",
            "Labrador Retriever",
            "Labrastaff",
            "Labsky",
            "Lagotto Romagnolo",
            "Lakeland Terrier",
            "Lancashire Heeler",
            "Leonberger",
            "Lhasa Apso",
            "Lowchen",
            "Maltese",
            "Maltese Shih Tzu",
            "Maltipoo",
            "Machester Terrier",
            "Mastador",
            "Mastiff",
            "Miniature Pinscher",
            "Miniature Schnauzer",
            "Morkie",
            "Mudi",
            "Mutt",
            "Neapolitan Mastiff",
            "Newfoundland",
            "Norfolk Terrier",
            "Norwegian Buhund",
            "Norwegian Elkhound",
            "Norwegian Lundehund",
            "Norwich Terrier",
            "Nova Scotia Duck Tolling Retriever",
            "Old English Sheepdog",
            "Otterhound",
            "Papillon",
            "Peekapoo",
            "Pekingese",
            "Pembroke Welsh Corgi",
            "Petit Basset Griffon Vendeen",
            "Pharaoh Hound",
            "Pitsky",
            "Plott",
            "Pocket Beagle",
            "Pointer",
            "Polish Lowland Sheepdog",
            "Pomapoo",
            "Pomchi",
            "Pomeagle",
            "Pomeranian",
            "Pomsky",
            "Poochon",
            "Poodle",
            "Portuguese Podengo Pequeno",
            "Portuguese Water Dog",
            "Pug",
            "Pugalier",
            "Puggle",
            "Puli",
            "Pyrenean Shepherd",
            "Rat Terrier",
            "Redbone Coonhound",
            "Rhodesian Ridgeback",
            "Rottador",
            "Rottle",
            "Rottweiler",
            "Saint Berdoodle",
            "Saint Bernard",
            "Saluki",
            "Samoyed",
            "Samusky",
            "Schipperke",
            "Schnoodle",
            "Scottish Deerhound",
            "Scottish Terrier",
            "Sealyham Terrier",
            "Shepsky",
            "Shetland Sheepdog",
            "Shiba Inu",
            "Shichon",
            "Shih-poo",
            "Shih Tzu",
            "Shiranian",
            "Shollie",
            "Shorkie",
            "Siberian Husky",
            "Silken Windhound",
            "Silky Terrier",
            "Skye Terrier",
            "Sloughi",
            "Small Munsterlander Pointer",
            "Soft Coated Wheaten Terrier",
            "Springador",
            "Stabyhoun",
            "Staffordshire Bull Terrier",
            "Standard Schnauzer",
            "Sussex Spaniel",
            "Swedish Vallhund",
            "Tibetan Mastiff",
            "Tibetan Spaniel",
            "Tibetan Terrier",
            "Toy Fox Terrier",
            "Treeing Tennessee Brindle",
            "Treeing Walker Coonhound",
            "Valley Bulldog",
            "Vizsla",
            "Weimaraner",
            "Welsh Springer Spaniel",
            "Welsh Terrier",
            "West Highland White Terrier",
            "Westiepoo",
            "Whippet",
            "Whoodle",
            "Wirehaired Pointing Griffon",
            "Xoloitzcuintli",
            "Yorkipoo",
            "Yorkshire Terrier"

        )


        val cat= arrayOf(
            "Abyssinian",
            "American Bobtail",
            "American Bobtail Shorthair",
            "American Curl",
            "American Curl Longhair",
            "American Shorthair",
            "American Wirehair",
            "Balinese",
            "bengal",
            "Birman",
            "Bombay",
            "British Shorthair",
            "British Longhair",
            "Burmese",
            "Chartreuz",
            "Chausie",
            "Cornish Rex",
            "Cymric",
            "Devon Rex",
            "Egyptian Mau",
            "Exotic Shorthair",
            "Havana",
            "Himalayan",
            "Japanese Bobtail",
            "Japanese Bobtail Longhair",
            "Korat",
            "Kurilian Bobtail",
            "Kurilian Bobtail Longhair",
            "LaPerm",
            "LaPerm Shorthair",
            "Maine Coon",
            "Manx",
            "Munchkin",
            "Munchkin Longhair",
            "Nebelung",
            "Norwegian Forest",
            "Ocicat",
            "Oriental Longhair",
            "Oriental Shorthair",
            "Persian",
            "Peterbald",
            "Pixiebob",
            "Pixiebob Longhair",
            "Ragdoll",
            "Russian Blue",
            "Savannah",
            "Scottish Fold",
            "Scottish Fold Longhair",
            "Selkirk Rex",
            "Selkirk Rex Longhair",
            "Siamese",
            "Siberian",
            "Singapura",
            "Snowshoe",
            "Somali",
            "Sphynx",
            "Thai",
            "Tonkinese",
            "Toyger",
            "Turkish Angora",
            "Turkish Van",
            "Household Pet",
            "Household Pet Kitten",
            "Australian Mist",
            "Burmilla",
            "Khaomanee",
            "Napolean",
            "Napolean Longhair",
            "Serengeti",
            "Donskoy",
            "Highlander",
            "Highlander Shorthair",
            "Minskin",
            "Ojos Azules",
            "Ojos Azules Longhair"
        )

        Log.e("pet type","//////////////// $petType")


        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {
                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)

                when {

                    viewdata.petCategoryId == "Cat" -> {

                        petType = 2


                        views.auto_complete_text_view.post(Runnable {



                            val adapter = ArrayAdapter<String>(
                                views.context,
                                android.R.layout.simple_dropdown_item_1line,
                                cat
                            )
                            views.auto_complete_text_view.setAdapter(adapter)


                        })

                    }

                    viewdata.petCategoryId == "Dog" -> {

                        petType = 1

                        views.auto_complete_text_view.post(Runnable {


                            val adapter = ArrayAdapter<String>(
                                views.context,
                                android.R.layout.simple_dropdown_item_1line,
                                dog
                            )
                            views.auto_complete_text_view.setAdapter(adapter)


                        })


                    }

                }


            } catch (e: Exception) {
                val s = e.message
                Log.e("Error in breed",s)
            }
        }

        views.auto_complete_text_view.threshold = 1
        views.auto_complete_text_view.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            process(selectedItem)
            //Toast.makeText(views.context,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }
        views.auto_complete_text_view.setOnDismissListener {
            //  Toast.makeText(views.context,"Suggestion closed.", Toast.LENGTH_SHORT).show()
            // process(views.auto_complete_text_view.text.toString())
        }
        views.auto_complete_text_view.onFocusChangeListener = View.OnFocusChangeListener{

                view, b ->


            if(b){




            }

        }






        return views
    }
    private fun process(breed:String) {
        Log.e("function called...","working")
        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {

                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)
                var pet = petMasterEntity(
                    petId = petId,
                    userId= user,
                    petName = viewdata.petName,
                    age = viewdata.age,
                    sex = viewdata.sex,
                    petCategoryId = viewdata.petCategoryId,
                    breedId= breed
                )
                val callDetails = splootDB!!.petMasterDao().update(pet)

                val get = splootDB!!.petMasterDao().getSelect(petid!!)

                Log.e("tabledata",""+get)

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error in breed",s)
            }
        }
    }
}