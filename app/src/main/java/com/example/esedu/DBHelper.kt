package com.example.esedu
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date

class DBHelper(context : Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val queryUser = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, pass TEXT)"
        db!!.execSQL(queryUser)
        }
    fun addUser(user: User){
        val values = ContentValues()

        values.put("login", user.login)
        values.put("pass", user.pass)


        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }
    fun isUsernameAvailable(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM users WHERE login = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.rawQuery(query, selectionArgs)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count == 0
    }

    fun createUser() {
        val db = this.writableDatabase
        val queryUser = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, pass TEXT)"
        db!!.execSQL(queryUser)
    }
    fun getUser(login: String, pass: String) : Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$pass'", null)
        return result.moveToFirst()
    }
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase

        val query = "SELECT * FROM users"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "User comp")
                val loginIndex = cursor.getColumnIndex("login")
                val passIndex = cursor.getColumnIndex("pass")

                if (loginIndex != -1 && passIndex != -1) {
                    val login = cursor.getString(loginIndex)
                    val pass = cursor.getString(passIndex)

                    val user = User(login, pass)
                    userList.add(user)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return userList
    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS soups")
        db.execSQL("DROP TABLE IF EXISTS fits")
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS dish")


        db.execSQL("DROP TABLE IF EXISTS category")
        onCreate(db)
    }
    fun deleteItem(name: String):Int {
        val x = getItemIdByName(name)
        val y = getItemTableByName(name)
        Log.e("e", y)
        val db = this.writableDatabase
        Log.e("E", "deleting item from $y where id = $x, with name $name")
        return db.delete(y, "id = ?", arrayOf(x.toString()))
    }
    fun updateTable(idx: Int, name: String, composition: String, price: Double, image: String, tableName: String) {
        val tableNameZ = getItemTableByName(tableName.trim())
        if (tableNameZ.isNotEmpty()) {
            Log.e("E", "Обновление таблицы $tableNameZ, id: $idx, name: $name")
            val db = this.writableDatabase
            db.execSQL("UPDATE $tableNameZ SET name = ?, composition = ?, price = ?, image = ? WHERE id = ?", arrayOf(name, composition, price, image, idx))
        } else {
            Log.e("E", "Таблица не найдена для элемента с именем $tableName")
        }
    }
    fun getItemIdByName(name: String): Int {
        val db = this.readableDatabase
        var itemId = -1

        val tables = arrayOf("soups", "fits", "dish", "desert", "colds", "drinks")

        for (table in tables) {
            val query = "SELECT id FROM $table WHERE name LIKE ?"
            val cursor = db.rawQuery(query, arrayOf(name))

            if (cursor.moveToFirst()) {
                val itemIdIndex = cursor.getColumnIndex("id")
                if (itemIdIndex != -1) {
                    itemId = cursor.getInt(itemIdIndex)
                    break
                }
            }
            cursor.close()
        }
        return itemId
    }
    fun getItemTableByName(name: String): String {
        val db = this.readableDatabase
        var tableName: String = ""
        val tables = arrayOf("soups", "fits", "dish", "desert", "colds", "drinks")

        for (table in tables) {
            val query = "SELECT id FROM $table WHERE name LIKE ?"
            val cursor = db.rawQuery(query, arrayOf(name))

            if (cursor.moveToFirst()) {
                val itemIdIndex = cursor.getColumnIndex("id")
                if (itemIdIndex != -1) {
                    cursor.close()
                    tableName = table
                    break
                }
            }
            cursor.close()
        }

        return tableName
    }
    fun searchItem(query: String): Any? {
        val db = this.readableDatabase
        var result: Any? = null

        // Поиск в таблице soups
        val soupQuery = "SELECT * FROM soups WHERE name LIKE '%$query%'"
        val soupCursor = db.rawQuery(soupQuery, null)
        if (soupCursor.moveToFirst()) {
            // Если элемент найден в таблице soups, создаем объект Soup и возвращаем его
            val nameIndex = soupCursor.getColumnIndex("name")
            val categoryIDIndex = soupCursor.getColumnIndex("categoryID")
            val compositionIndex = soupCursor.getColumnIndex("composition")
            val priceIndex = soupCursor.getColumnIndex("price")
            val legendIndex = soupCursor.getColumnIndex("legend")
            val imageIndex = soupCursor.getColumnIndex("image")
            Log.e("E","Searching in soups")
            val name = soupCursor.getString(nameIndex)
            val categoryID = soupCursor.getInt(categoryIDIndex)
            val composition = soupCursor.getString(compositionIndex)
            val price =  soupCursor.getDouble(priceIndex)
            val legend = soupCursor.getString(legendIndex)
            val image = soupCursor.getString(imageIndex)

            result = Soup(name, categoryID, composition, price, legend, image)
            return result

        }
        soupCursor.close()

        // Поиск в таблице fits
        val fitQuery = "SELECT * FROM fits WHERE name LIKE '%$query%'"
        val fitCursor = db.rawQuery(fitQuery, null)
        if (fitCursor.moveToFirst()) {
            val nameIndex = fitCursor.getColumnIndex("name")
            val categoryIDIndex =fitCursor.getColumnIndex("categoryID")
            val compositionIndex =fitCursor.getColumnIndex("composition")
            val priceIndex =fitCursor.getColumnIndex("price")
            val imageIndex =fitCursor.getColumnIndex("image")
            Log.e("E","Searching in fits")

            val name =fitCursor.getString(nameIndex)
            val categoryID =fitCursor.getInt(categoryIDIndex)
            val composition =fitCursor.getString(compositionIndex)
            val price = fitCursor.getDouble(priceIndex)
            val image = fitCursor.getString(imageIndex)

            result = FitBurger(name, categoryID, composition, price,  image)
            return result

        }
        fitCursor.close()

        val dishQuery = "SELECT * FROM dish WHERE name LIKE '%$query%'"
        val dishCursor = db.rawQuery(dishQuery, null)
        if (dishCursor.moveToFirst()) {
            val nameIndex = dishCursor.getColumnIndex("name")
            val categoryIDIndex = dishCursor.getColumnIndex("categoryID")
            val compositionIndex = dishCursor.getColumnIndex("composition")
            val priceIndex = dishCursor.getColumnIndex("price")
            val imageIndex = dishCursor.getColumnIndex("image")
            Log.e("E","Searching in dish")


            val name = dishCursor.getString(nameIndex)
            val categoryID = dishCursor.getInt(categoryIDIndex)
            val composition = dishCursor.getString(compositionIndex)
            val price =  dishCursor.getDouble(priceIndex)
            val image = dishCursor.getString(imageIndex)

            result = Dish(name, categoryID, composition, price,  image)
            return result

        }
        dishCursor.close()

        val desertQuery = "SELECT * FROM desert WHERE name LIKE '%$query%'"
        val desertCursor = db.rawQuery(desertQuery, null)
        Log.e("E","Searching in desert")

        if (desertCursor.moveToFirst()) {
            val nameIndex = desertCursor.getColumnIndex("name")
            val categoryIDIndex = desertCursor.getColumnIndex("categoryID")
            val compositionIndex = desertCursor.getColumnIndex("composition")
            val priceIndex = desertCursor.getColumnIndex("price")
            val imageIndex = desertCursor.getColumnIndex("image")
            Log.e("E","Searching in desert")

            val name = desertCursor.getString(nameIndex)
            val categoryID = desertCursor.getInt(categoryIDIndex)
            val composition = desertCursor.getString(compositionIndex)
            val price =  desertCursor.getDouble(priceIndex)
            val image = desertCursor.getString(imageIndex)

            result = Desert(name, categoryID, composition, price,  image)
            return result

        }
        desertCursor.close()

        val drinksQuery = "SELECT * FROM drinks WHERE name LIKE '%$query%'"
        val drinksCursor = db.rawQuery(drinksQuery, null)
        Log.e("E","Searching in drinks")

        if (drinksCursor.moveToFirst()) {
            val nameIndex = drinksCursor.getColumnIndex("name")
            val categoryIDIndex = drinksCursor.getColumnIndex("categoryID")
            val compositionIndex = drinksCursor.getColumnIndex("composition")
            val priceIndex = drinksCursor.getColumnIndex("price")
            val imageIndex = drinksCursor.getColumnIndex("image")
            Log.e("E","Searching in drinks")

            val name = drinksCursor.getString(nameIndex)
            val categoryID = drinksCursor.getInt(categoryIDIndex)
            val composition = drinksCursor.getString(compositionIndex)
            val price =  drinksCursor.getDouble(priceIndex)
            val image = drinksCursor.getString(imageIndex)

            result = Desert(name, categoryID, composition, price,  image)
            return result

        }
        drinksCursor.close()

        val coldsQuery = "SELECT * FROM colds WHERE name LIKE '%$query%'"
        val coldsCursor = db.rawQuery(coldsQuery, null)
        Log.e("E","Searching in colds")
        if (coldsCursor.moveToFirst()) {
            val nameIndex = coldsCursor.getColumnIndex("name")
            val categoryIDIndex = coldsCursor.getColumnIndex("categoryID")
            val compositionIndex = coldsCursor.getColumnIndex("composition")
            val priceIndex = coldsCursor.getColumnIndex("price")
            val imageIndex = coldsCursor.getColumnIndex("image")
            Log.e("E","Reaching in colds")

            val name = coldsCursor.getString(nameIndex)
            val categoryID = coldsCursor.getInt(categoryIDIndex)
            val composition = coldsCursor.getString(compositionIndex)
            val price =  coldsCursor.getDouble(priceIndex)
            val image = coldsCursor.getString(imageIndex)

            result = ColdSoup(name, categoryID, composition, price,  image)
            return result

        }
        coldsCursor.close()
        return result
    }
    fun checkUserExistence(userId: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM result WHERE userID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        var userExists = false

        if (cursor != null) {
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            userExists = count > 0
            cursor.close()
        }

        return userExists
    }
    fun createSoup() {
        val querySoup = "CREATE TABLE soups (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, categoryID INTEGER, composition TEXT, price REAL, legend TEXT, image TEXT, FOREIGN KEY (categoryID) REFERENCES category(id))"
            val db = this.writableDatabase
            db!!.execSQL(querySoup)
        }
    fun addSoup(soup: Soup) {
        val values = ContentValues()
        values.put("name", soup.name)
        values.put("categoryID", soup.categoryID)
        values.put("composition", soup.composition)
        values.put("price", soup.price)
        values.put("legend", soup.Legend)
        values.put("image", soup.image)
        val db = this.writableDatabase
        db.insert("soups", null, values)
    }
    fun updateSoup(idx: Int, name: String, composition: String, price: Double, legend: String, image: String) {
        val db = this.writableDatabase
        Log.e("E", "Обновление таблицы soups, id: $idx, name: $name")
        db.execSQL("UPDATE soups SET name = ?, composition = ?, price= ?, legend = ?, image = ? where id = ?", arrayOf(name,composition,price,legend,image, idx))
    }
    fun insertSoups() {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO soups ( name, categoryID,composition, price, legend, image) VALUES\n" +
                "    ( 'Норвежский суп',1, 'картофель, морковь, лосось, сливки, укроп, смесь перцев, соль', 6.9, 'Вкуснейший суп с пользой для всего организма. Богат фосфором, который повышает работоспособность и укрепляет нервную систему. Содержит незаменимые кислоты омега-3, необходимые для клеток головного мозга и правильного функционирования сердца и сосудов.', 'norw'),\n" +
                "    ( 'Борщ по-украински',1, 'свинина, свекла, картофель, морковь, томатная паста, перец свежий, масло растительное, лук, чеснок, сахар, уксус, укроп, соль, смесь перцев, лавровый лист, сметана', 6.5, 'Основной ингредиент – местный супер-фуд – СВЕКЛА. Благодаря витаминам и минералам (К, С, группа В, магний, железо) свекла укрепит иммунитет, улучшит кровообращение в головном мозге.', 'borsch'),\n" +
                "    ( 'Сырный суп',1, 'куриное филе, картофель, вермишель, сыр плавленый, лук, морковь, куркума, укроп, петрушка, морская соль, перец', 6.5, 'Содержит витамины В1, В2, В12, РР, С, К, необходимые организму минеральные вещества: калий, кальций, железо, фосфор и натрий, йод, незаменимые жирные аминокислоты.', 'jeesus'),\n" +
                "    ( 'Суп из цыплёнка',1, 'цветная капуста, горошек зеленый, куриное филе, морковь, лук репчатый, масло растительное, мука, масло сливочное, хмели-сунели, приправа гурман, соль, смесь перцев', 6.5, 'Цветная капуста и зеленый горошек, входящие в состав этого супа, насытят организм витаминами А и С, такими важными для крепкого иммунитета витаминами группы В.', 'chicken'),\n" +
                "    ( 'Турецкий чечевичный суп',1, 'чечевица зеленая, булгур, лук, томатная паста, масло сливочное, паприка, тимьян сушеный, кумин, перец чили красный, соль', 5.9, 'Диетическая альтернатива супам с мясом. Чечевица богата легкоусвояемым белком, а также железом, витамином B1, аминокислотами и микроэлементами.', 'turkish'),\n" +
                "    ( 'Рождественский гороховый суп',1, 'бульба, сыр плаўлены, гарох, каўбаскі, морква, цыбуля, алей, пятрушка, лаўровы ліст, соль, сумесь перцаў', 6.9, '', 'christmas'),\n" +
                "    ( 'Солянка',1, 'картофель, огурцы маринованные, лук, филе куриное, охотничьи колбаски, грудинка, томатная паста, маслины, масло растительное, укроп, лавровый лист, смесь перцев, соль, лимон, сметана', 9.9, 'Отварное куриное филе – источник быстроусвояемого высококачественного животного белка. Благотворно влияет на деятельность нервной системы и желудочно-кишечного тракта.', 'solanka'),\n" +
                "    ( 'Сливочный суп',1, 'шампиньоны, картофель, сливки, сливочный сыр, морковь, лук, соль, мускатный орех, лавровый лист, укроп, мука', 5.9, 'Шампиньоны полезны для организма, т.к. содержат такие минералы, как: селен, железо, цинк, кальций, йод и витамины группы В (В6, В2, В1), которые необходимы для работы нервной системы, здоровья кожи, волос и ногтей.', 'weed');")
    }
    fun getAllSoups(): List<Soup> {
        val soupList = mutableListOf<Soup>()
        val db = this.readableDatabase

        val query = "SELECT * FROM soups"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Soup com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val legendIndex = cursor.getColumnIndex("legend")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val legend = cursor.getString(legendIndex)
                    val image = cursor.getString(imageIndex)

                    val soup = Soup(name, categoryID,composition, price, legend, image)
                    soupList.add(soup)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return soupList
    }

    fun isTableExists(): Boolean {
        val db = this.readableDatabase
        val tableName = "soups"
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", arrayOf(tableName))
        val tableExists = cursor.moveToFirst()
        cursor.close()
        return tableExists
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun updateMark(userId: Int?, mark: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("mark", mark)
            Log.e("E", mark)
        }
        val selection = "userID = ?"
        val selectionArgs = arrayOf(userId.toString())
        db.update("result", values, selection, selectionArgs)
    }
    fun getMarkForUser(userId: Int): String? {
        val db = this.readableDatabase
        val query = "SELECT mark FROM result WHERE userID = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor = db.rawQuery(query, selectionArgs)
        var mark: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("mark")
            if (columnIndex >= 0) {
                mark = cursor.getString(columnIndex)
            }
        }
        cursor.close()
        return mark
    }


    fun createResultTable() {
        val db = this.writableDatabase
        val query = "CREATE TABLE result (id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, endingSoupTest DATE,endingFitTest date, endingDishTest date, endingDrinksTest date,endingDesertsTest date, mark TEXT, FOREIGN KEY (userID) REFERENCES users(id))"
        db!!.execSQL(query)
    }
    fun addResult(result: Result) {
        val values = ContentValues()

        values.put("userID", result.userID)
        values.put("endingSoupTest", result.endingSoupTest.toString())
        values.put("endingFitTest", result.endingFitTest.toString())
        values.put("endingDishTest", result.endingDishTest.toString())
        values.put("endingDrinksTest", result.endingDrinksTest.toString())
        values.put("endingDesertsTest", result.endingDesertsTest.toString())
        //  values.put("mark", result.mark)

        val db = this.writableDatabase
        db.insert("result", null, values)
        db.close()
    }
    fun getAllResults(): List<Result> {
        val resultList = mutableListOf<Result>()
        val db = this.readableDatabase

        val query = "SELECT * FROM result"
        val cursor = db.rawQuery(query, null)
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Result comp")
                val userIDIndex = cursor.getColumnIndex("userID")
                val endingSoupIndex = cursor.getColumnIndex("endingSoupTest")
                val endingFitIndex = cursor.getColumnIndex("endingFitTest")
                val endingDishIndex = cursor.getColumnIndex("endingDishTest")
                val endingDrinksIndex = cursor.getColumnIndex("endingDrinksTest")
                val endingDesertsIndex = cursor.getColumnIndex("endingDesertsTest")

                val userID = cursor.getInt(userIDIndex)
                val endingSoupTime = cursor.getString(endingSoupIndex)
                val endingDishTime = cursor.getString(endingDishIndex)
                val endingFitTime = cursor.getString(endingFitIndex)
                val endingDesertTime = cursor.getString(endingDesertsIndex)
                val endingDrinksTime = cursor.getString(endingDrinksIndex)


                val endingSoup = endingSoupTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDish = endingDishTime?.let { LocalDateTime.parse(it, formatter) }
                val endingFit = endingFitTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDrinks = endingDrinksTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDesert = endingDesertTime?.let { LocalDateTime.parse(it, formatter) }

                if (userIDIndex != -1 && endingSoupIndex != -1 && endingDishIndex != -1 && endingFitIndex != -1 && endingDrinksIndex != -1 && endingDesertsIndex != -1) {
                    val result = Result(userID, endingSoup, endingFit, endingDish, endingDrinks, endingDesert,"")
                    resultList.add(result)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return resultList
    }
    fun getResultsByUser(userId: Int, startDate: LocalDateTime, endDate: LocalDateTime): List<Result> {
        val resultList = mutableListOf<Result>()
        val db = this.readableDatabase

        val query = """
        SELECT * FROM result 
        WHERE userID = ? 
        AND (
            (endingSoupTest BETWEEN ? AND ?) OR
            (endingFitTest BETWEEN ? AND ?) OR
            (endingDishTest BETWEEN ? AND ?) OR
            (endingDrinksTest BETWEEN ? AND ?) OR
            (endingDesertsTest BETWEEN ? AND ?)
        )
    """

        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val startDateStr = startDate.format(formatter)
        val endDateStr = endDate.format(formatter)

        val cursor = db.rawQuery(query, arrayOf(
            userId.toString(),
            startDateStr, endDateStr,
            startDateStr, endDateStr,
            startDateStr, endDateStr,
            startDateStr, endDateStr,
            startDateStr, endDateStr
        ))

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Result comp")
                val userIDIndex = cursor.getColumnIndex("userID")
                val endingSoupIndex = cursor.getColumnIndex("endingSoupTest")
                val endingFitIndex = cursor.getColumnIndex("endingFitTest")
                val endingDishIndex = cursor.getColumnIndex("endingDishTest")
                val endingDrinksIndex = cursor.getColumnIndex("endingDrinksTest")
                val endingDesertsIndex = cursor.getColumnIndex("endingDesertsTest")
                val markIndex = cursor.getColumnIndex("mark")

                val userID = cursor.getInt(userIDIndex)
                val endingSoupTime = cursor.getString(endingSoupIndex)
                val endingDishTime = cursor.getString(endingDishIndex)
                val endingFitTime = cursor.getString(endingFitIndex)
                val endingDesertTime = cursor.getString(endingDesertsIndex)
                val endingDrinksTime = cursor.getString(endingDrinksIndex)

                val endingSoup = endingSoupTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDish = endingDishTime?.let { LocalDateTime.parse(it, formatter) }
                val endingFit = endingFitTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDrinks = endingDrinksTime?.let { LocalDateTime.parse(it, formatter) }
                val endingDesert = endingDesertTime?.let { LocalDateTime.parse(it, formatter) }
                val mark = cursor.getString(markIndex)

                if (userIDIndex != -1 && endingSoupIndex != -1 && endingDishIndex != -1 && endingFitIndex != -1 && endingDrinksIndex != -1 && endingDesertsIndex != -1) {
                    val result = Result(userID, endingSoup, endingFit, endingDish, endingDrinks, endingDesert,mark)
                    resultList.add(result)
                } else {
                    Log.e("E", "Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return resultList
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createDish() {
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE dish (id INTEGER PRIMARY KEY AUTOINCREMENT, name Text, categoryID INTEGER, composition TEXT, price REAL, image TEXT,  FOREIGN KEY (categoryID) REFERENCES category(id))")
    }
    fun addDish(dish:Dish) {
        val values = ContentValues()
        values.put("name", dish.name)
        values.put("categoryID", dish.categoryID)
        values.put("composition", dish.composition)
        values.put("price", dish.price)
        values.put("image", dish.image)
        val db = this.writableDatabase
        db.insert("dish", null, values)
    }
    fun insertDish() {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO dish(name, categoryID, composition, price, image) VALUES" +
                "('Паста Болоньезе',3,'спагетти, говядина, соус болоньезе (морковь, лук, томатная паста, томаты протертые, мука, сахар, лавровый лист, мускатный орех, смесь перцев, соль)',8.90,'pasta')," +
                "('Чикенболы с гречкой',3,'чикенболы (куриное филе, сметана, панировочные сухари, паприка, соль, перец) гречневая крупа, шампиньоны, масло растительное, соль, орегано',8.90,'chickenballs')," +
                "('Рыбные паровые котлеты', 3,'фарш из лосося, рис, морковь, брокколи, масло сливочное, лук, куркума, лимонный сок, смесь перцев, соль', 9.20, 'fish')," +
                "('Котлета домашняя',3,'пюре картофельное (картофель, молоко, масло сливочное, соль), котлета (фарш куриный, хлеб, яйца куриные, смесь перцев, соль), соус (молоко, томатная паста, лук, масло сливочное, мука, смесь перцев, соль)',9.20,'dom')," +
                "('Салат Цезарь',4,'томаты черри, куриное филе жареное, соус Цезарь (майонез, горчица, соевый соус, чеснок), яйца куриные, салат ромен, сыр моцарелла, сухарики', 9.90, 'saladcesar')," +
                "('Салат Грузинский с печеными овощами', 4, 'свекла, тыква, капуста пекинская, сыр фета, грецкий орех, заправка соевая (соевый соус, масло оливковое, лимонный сок, сахар)', 5.50, 'saladgorgi')," +
                "('Салат Лёгкий',4, 'пекинская капуста, морковь, масло растительное, лук красный, укроп, морская соль, смесь перцев', 4.50,'saladeasy')," +
                "('Салат Нежный',4, 'пекинская капуста, куриное филе, ананас консервированный, майонез, стебель сельдерея, горчица, семена подсолнечника', 5.90,'saladsoft')")
    }
    fun getAllDish(): List<Dish> {
        val dishList = mutableListOf<Dish>()
        val db = this.readableDatabase

        val query = "SELECT * FROM dish"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Dish com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val image = cursor.getString(imageIndex)

                    val dish = Dish(name, categoryID,composition, price,  image)
                    dishList.add(dish)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return dishList
    }
    fun createFit() {
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE fits (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,categoryID INTEGER, composition TEXT, price REAL, image TEXT, FOREIGN KEY (categoryID) REFERENCES category(id))")
    }
    fun addFit(fit:FitBurger) {
        val values = ContentValues()
        values.put("name", fit.name)
        values.put("categoryID", fit.categoryID)
        values.put("composition", fit.composition)
        values.put("price", fit.price)
        values.put("image", fit.image)
        val db = this.writableDatabase
        db.insert("fits", null,values)
    }
    fun insertFits() {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO fits (name, categoryID, composition, price, image) VALUES " +
                "('Сомерсет с грибами',2, 'чиабатта, шампиньоны, сливочный сыр, сыр чеддер, масло растительное, укроп, соль, смесь перцев', 9.9, 'somer'), " +
                "('Барселона с куриной котлетой',2, 'чиабатта, филе куриное, соус (майонез, горчица, соус гриль), сыр моцарелла, томаты, лук репчатый, хлеб тостовый, капуста пекинская, огурцы маринованные, соль, смесь перцев', 11.5, 'barspng'), " +
                "('Каталония с чоризо',2, 'чиабатта, фасоль стручковая, чоризо, майонез, томаты, руккола, горчица французская, лук красный, чеснок', 10.9, 'katal'), " +
                "('Цезарь с цыпленком',2, 'чиабатта, куриное филе, яйца куриные, томаты, майонез, сыр моцарелла, пекинская капуста, горчица французская, соевый соус, масло растительное, чеснок, карри, перец чили, тимьян, морская соль, смесь перцев', 11.9, 'cesar'), " +
                "('Техас с говядиной',2, 'чиабатта, говядина, капуста краснокочанная, огурцы маринованные, морковь, соус барбекю, майонез, лук красный, лук зеленый, уксус', 12.5, 'texas'), " +
                "('Прованс с цыпленком',2, 'чиабатта, куриное филе, грибы, майонез, грудинка, пекинская капуста, масло, укроп, чеснок, морская соль, средиземноморские травы, смесь перцев', 11.5, 'prov'), " +
                "('Нур-Норге с тунцом',2, 'чиабатта, тунец консервированный, томаты, яйцо куриное, ромен-салат, лук красный, лук зеленый, укроп, майонез', 10.9, 'nur'), " +
                "('Афины с тыквой',2, 'чиабатта, тыква, капуста брокколи, сыр моцарелла, салат ромен, сливки, сыр сливочный, лук, укроп', 8.9, 'ath');")

    }
    fun getAllFits(): List<FitBurger> {
        val fitList = mutableListOf<FitBurger>()
        val db = this.readableDatabase

        val query = "SELECT * FROM fits"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Fits com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val image = cursor.getString(imageIndex)

                    val fit = FitBurger(name, categoryID,composition, price, image)
                    fitList.add(fit)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return fitList
    }
    fun createCategory(){
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE category (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, seasonal INTEGER, alcohol INTEGER)")
    }
    fun insertCategory(){
        val db = this.writableDatabase
        db.execSQL("insert into category (name, seasonal, alcohol) VALUES" +
                "('Суп', 0, 0)," +          //1
                "('FitBurger', 0,0)," +     //2
                "('Горячее блюдо',0,0)," +  //3
                "('Салат',0,0)," +          //4
                "('Десерт',0,1)," +         //5
                "('Чай',0,1)," +            //6
                "('Холодные напитки',1,1)," +//7
                "('Комбуча',0,0)," +        //8
                "('Лимонады',1,0)," +       //9
                "('Охлаждённые cупы',0,0)," +//10
                "('Холодные сендвичи',0,0)," +//11
                "('Питьевые супы',0,0)," +//12
                "('Хлеб', 0,0)")            //13
    }
    fun createDesert(){
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE desert (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, categoryID INTEGER, composition TEXT, price REAL, image TEXT, FOREIGN KEY (categoryID) REFERENCES category(id))")
    }
    fun addDesert(desert: Desert) {
        val values = ContentValues()
        values.put("name", desert.name)
        values.put("categoryID", desert.categoryID)
        values.put("composition", desert.composition)
        values.put("price", desert.price)
        values.put("image", desert.image)
        val db = this.writableDatabase
        db.insert("desert", null, values)
    }
    fun insertDesert(){
        val db = this.writableDatabase
        db.execSQL("insert into desert(name, categoryID, composition, price, image) VALUES" +
                "('Чиа',5,'молоко кокосовое, семена чиа, черная смородина, вода, сахар, мята', 6.5,'chia')," +
                "('Лимонный чизкейк', 5, 'сыр сливочный, сметана, сливки, вода, сахар, песочное печенье (мука пшеничная в/с, масло сливочное, сахар, яйца куриные, разрыхлитель, соль), лимон, желатин', 6.2, 'lem')," +
                "('Наполеон', 5, 'молоко, тесто слоеное бездрожжевое, сахар, яйца куриные, масло сливочное, крахмал кукурузный, ванилин', 5.9, 'napoleon')")
    }
    fun getAllDeserts(): List<Desert> {
        val desertList = mutableListOf<Desert>()
        val db = this.readableDatabase

        val query = "SELECT * FROM desert"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Deserts com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val image = cursor.getString(imageIndex)

                    val desert = Desert(name, categoryID,composition, price,  image)
                    desertList.add(desert)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return desertList
    }
    fun createDrinks(){
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE drinks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, categoryID INTEGER, composition TEXT, price REAL, image TEXT, FOREIGN KEY (categoryID) REFERENCES category(id))")
    }
    fun addDrinks(drinks: Drinks) {
        val values = ContentValues()
        values.put("name", drinks.name)
        values.put("categoryID", drinks.categoryID)
        values.put("composition", drinks.composition)
        values.put("price", drinks.price)
        values.put("image", drinks.image)
        val db = this.writableDatabase
        db.insert("drinks", null, values)
    }
    fun insertDrinks(){
        val db = this.writableDatabase
        db.execSQL("insert into drinks(name, categoryID, composition, price, image) VALUES" +
                "('Имбирный эль',8, 'сахар, лимон, имбирь, дрожжи, вода', 4.9,'imbir')," +
                "('Комбуча Love', 8,'чай черный байховый, чай зелёный, цветки розы суданской, культура чайного гриба, сахар, вода', 4.9,'love' )," +
                "('Камбуча Калядная',8,'грачаная гарбата, чорная гарбата, кавалачкі цынамона, ажына ліст, цэдра апельсіна, маліны кавалачкі, перац ружовы гарошак, васілёк пялёсткі, куркума, камбуча, цукар, вада', 4.9, 'kalad')," +
                "('Комбуча Жасмин', 8 , 'чай зеленый, цветки жасмина, культура чайного гриба, сахар, вода', 4.9, 'jasmine')," +
                "('Комбуча Молочный улун', 8, 'чай зеленый молочный улун, культура чайного гриба, сахар, вода',4.9,'oolong')," +
                "('Комбуча Лаванда', 8, 'чай зеленый, цветки клитории, чай черный байховый,  цветки лаванды, культура чайного гриба, сахар, вода', 4.9, 'lavander')," +
                "('Комбуча Лемонграсс', 8 , 'чай черный байховый, лемонграсс, культура чайного гриба, сахар, вода', 4.9, 'lemongrass')," +
                "('Комбуча Мята', 8, 'чай черный байховый, мята, культура чайного гриба, сахар, вода', 4.9, 'mint')," +
                "('Ананасово-имбирный пунш', 6, 'ананас, имбирь, лимон, корица, тимьян, сахар', 3.9, 'punsh')," +
                "('Ягодный чай Облепиха', 6, 'Облепиха, тимьян, лимон, сахар', 3.9, 'oblepiha')")
    }
    fun getAllDrinks(): List<Drinks> {
        val drinkList = mutableListOf<Drinks>()
        val db = this.readableDatabase

        val query = "SELECT * FROM drinks"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Drinks com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val image = cursor.getString(imageIndex)

                    val drinks = Drinks(name, categoryID,composition, price,  image)
                    drinkList.add(drinks)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return drinkList
    }
    fun createColds() {
        val db = this.writableDatabase
        db.execSQL("CREATE TABLE colds (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, categoryID INTEGER, composition TEXT, price REAL, image TEXT, FOREIGN KEY (categoryID) REFERENCES category(id))")
    }
    fun addColdScoldS(colds : ColdSoup) {
        val values = ContentValues()
        values.put("name", colds.name)
        values.put("categoryID", colds.categoryID)
        values.put("composition", colds.composition)
        values.put("price", colds.price)
        values.put("image", colds.image)
        val db = this.writableDatabase
        db.insert("colds", null, values)
    }
    fun insertColds() {
        val db = this.writableDatabase
        db.execSQL("insert into colds(name,categoryID, composition, price, image) VALUES" +
                "('Сырный суп с вермишелью 1 кг', 10, 'куриное филе, картофель, вермишель, сыр плавленый, лук, морковь, куркума, укроп, петрушка, соль, перец', 14.90,'cheesekg')," +
                "('Борщ по-украински 1 кг', 10, 'cвинина, свекла, картофель, морковь, томатная паста, перец свежий, масло растительное, лук, чеснок, сахар, уксус, укроп, морская соль, смесь перцев, лавровый лист, сметана', 15.90, 'borschkg')," +
                "('Куриный бульон 1 кг', 10, 'куриный суповой набор, лук, морковь, сельдерей, морская соль, укроп, смесь перцев', 9.9, 'chickensok')," +
                "('Костный бульон 1 кг', 10, 'говяжьи кости, куриный суповой набор, лук, морковь, лук-порей, растительное масло, корень имбиря, петрушка, морская соль, чеснок',10.9,'boneskg')," +
                "('Куриный сок', 12, 'куриное филе, картофель, вермишель, сыр плавленый, лук, морковь, куркума, укроп, петрушка, морская соль, перец', 5.9, 'chickensoup')," +
                "('Сливочный суп с грибами питьевой', 12, 'картофель, морковь, лук, шампиньоны, сыр сливочный, масло растительное, мускатный орех, укроп, соль, смесь перцев', 5.9, 'shroom')," +
                "('Куриный бульон', 12, 'куриный суповой набор, лук, морковь, сельдерей, морская соль, укроп, смесь перцев', 4.9, 'chickendrink')," +
                "('Костный бульон', 12, 'органические говяжьи кости, куриный суповой набор, лук, морковь, лук-порей, растительное масло, корень имбиря, петрушка, морская соль, чеснок', 5.9, 'bonesdrink')," +
                "('Сырный суп с вермишелью охлажденный', 10, 'куриное филе, картофель, вермишель, сыр плавленый, лук, морковь, куркума, укроп, петрушка, соль, перец', 6.8, 'cheesemini')," +
                "('Сливочный суп с шампиньонами охлажденный',10,'шампиньоны, картофель, сливки, сливочный сыр, морковь, лук,  соль, мускатный орех, лавровый лист, укроп, мука', 6.8,'shroomini')," +
                "('Английский стю охлажденный', 10, 'говядина, томатная паста, картофель, морковь, фасоль красная, горошек, стручковая фасоль, чеснок, перечная смесь, лавровый лист, сахар, паприка, соевый соус, масло растительное, лук репчатый, соль', 6.8, 'stfu')," +
                "('Борщ по-украински охлажденный', 10, 'свинина, свекла, картофель, морковь, томатная паста, перец свежий, масло растительное, лук, чеснок, сахар, уксус, укроп, соль, смесь перцев, лавровый лист, сметана', 6.8, 'borschmini')," +
                "('Холодный сэндвич с индейкой', 11, 'хлеб Харрис пшенично-отрубной, индейка су-вид, томаты, майонез, соус гриль, пекинская капуста, морская соль, кориандр, смесь перцев, мускатный орех, имбирь', 6.9, 'hsind')," +
                "('Холодный сэндвич с цыпленком', 11, 'хлеб Харрис пшенично-отрубной, куриное филе, томаты, пекинская капуста, майонез, соус сладкий чили, смесь перцев, соль', 6.9, 'hschic')," +
                "('Холодный сэндвич с ветчиной', 11, 'хлеб Харрис пшенично-отрубной, ветчина, сыр моцарелла, томаты, пекинская капуста, петрушка, майонез', 6.9,'hsvet')")
    }
    fun getAllColds(): List<ColdSoup> {
        val coldsList = mutableListOf<ColdSoup>()
        val db = this.readableDatabase

        val query = "SELECT * FROM colds"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("E", "Colds com")
                val nameIndex = cursor.getColumnIndex("name")
                val categoryIDIndex = cursor.getColumnIndex("categoryID")
                val compositionIndex = cursor.getColumnIndex("composition")
                val priceIndex = cursor.getColumnIndex("price")
                val imageIndex = cursor.getColumnIndex("image")
                if (nameIndex != -1 && compositionIndex != -1) {
                    val name = cursor.getString(nameIndex)
                    val categoryID = cursor.getInt(categoryIDIndex)
                    val composition = cursor.getString(compositionIndex)
                    val price =  cursor.getDouble(priceIndex)
                    val image = cursor.getString(imageIndex)

                    val coldSoup = ColdSoup(name, categoryID,composition, price,  image)
                    coldsList.add(coldSoup)
                } else {
                    Log.e("E","Error 1")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return coldsList
    }
    fun dropTables() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS soups")
        db.execSQL("Drop table if exists users")
        db.execSQL("DROP TABLE IF EXISTS drinks")
        db.execSQL("DROP TABLE IF EXISTS fits")
        db.execSQL("DROP TAble if exists dish")
        db.execSQL("DROP TABLE IF EXISTS desert")
        db.execSQL("DROP TABLE IF EXISTS colds")
        db.execSQL("DROP TABLE IF EXISTS result")
        db.execSQL("Drop table if exists category")
    }
    fun createTables() {
        createCategory()
        createDish()
        createFit()
        createUser()
        createDesert()
        createSoup()
        createDrinks()
        createColds()
        createResultTable()
        Log.i("I", "Таблицы созданы")
    }
    fun insertTables() {
        insertCategory()
        insertFits()
        insertSoups()
        insertDish()
        insertDesert()
        insertColds()
        insertDrinks()
    }
}