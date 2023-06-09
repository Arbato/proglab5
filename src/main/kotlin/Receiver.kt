
import constituents.Color
import constituents.Person
import java.lang.reflect.Field
import java.time.LocalDateTime
import java.util.*

/**
 * Reciever class, works with collection of people
 */

class Receiver(view: ViewMode) {
    private val people: Vector<Person> = Vector<Person>()
    private val creationDate: LocalDateTime = LocalDateTime.now()
    private var view = view
    private var max_id = 1
    var exit = false

    init{
        this.view.setRec(this);
    }

    fun help() {
        view.help()
    }

    fun exit() {
        exit = true
        view.exit()
    }

    fun getterExit(): Boolean {return this.exit}

    fun addElement(name: String=""){
        for (i in people){
            if (i.getId() >= max_id){
                max_id = i.getId()+1
            }
        }
        val newbie = view.readElement(name, Person())
        if (newbie != null){
            newbie.setId(max_id)
            people.add(newbie)
            //view.success()
        } else {
            throw Exception()
            //view.fail("Adding Person failed!")
        }
    }


    fun pushPerson(p: Person){
        this.people.add(p)
    }

    fun show(){
        view.show(people)
    }

    fun info(){
        try {
            val stackField: Field = Receiver::class.java.getDeclaredField("people")
            val stackType = stackField.genericType.typeName
            if (!people.isEmpty()) {
                view.info(people, creationDate, stackType)
            } else {
                view.infoProblem()
            }
        } catch (ex: NoSuchFieldException) {
            view.print("Problem with general class. Can not find type of class!")
        }
    }

    fun updateId(){
        val compPerson = Person()
        view.readElement("", compPerson)
        var newId = 0
        while(newId == 0){
            try{newId = view.inputId()} catch(ex: Exception){println(ex.message)}
        }

        if (newId<1){
            view.fail("ID must be positive!")
            throw Exception()

        }

        for(p in people){
            if (p.getId() == newId){
                view.fail("This ID already exists!")
                throw Exception()

            }
        }
        for(p in people){
            if (p.compareFields(compPerson)){
                p.setId(newId)
                return
            }
        }
    }

    fun removeById(id: Int){
        for (p in people){
            if (p.getId() == id){
                people.remove(p)
            }
        }
    }

    fun clear(){
        people.removeAllElements()

    }
    fun saveToOther(){
        try {
            var path = view.getPath()
            saveToDataBase(path, people)
        }

        catch (ex: Exception) {
            view.fail("Try again, check your path.")
            throw Exception()
        }
    }


    fun rmGreater(i: Int){
        var collector : Vector<Person> = Vector<Person>()
        for(p in people){
            if(i < p.getId()){
                collector.add(p)
            }
        }
        people.removeAll(collector)

    }

    fun rmLesser(i: Int){
        var collector : Vector<Person> = Vector<Person>()
        for(p in people){
            if(i > p.getId()){
                collector.add(p)
            }
        }
        people.removeAll(collector)
    }

    fun fHairColor(color: Color){
        for(p in people){
            if (p.getHairColor() === color){
                view.print(p.toString())
            }
        }
    }

    fun decByHairColor(){
        people.sort()
        val it: Iterator<Person> = people.iterator()
        while (it.hasNext()) {
            view.print(it.next().toString())
        }
    }

    var byColor: Comparator<Person> = Comparator {person1: Person, person2: Person ->
        Integer.compare(
            person1.getHairColor()!!.ordinal,
            person2.getHairColor()!!.ordinal
        )
    }

    fun hairComparator(){
        people.sortWith(byColor)
    }

    fun executeScript(fileName : String){
        view.runScript(fileName)
    }

    fun reorder(){
        people.reverse()
    }
}