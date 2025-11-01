# How did you implement CRUD using SQLite?
I used a custom NoteDbHelper extending SQLiteOpenHelper. Create: addNote() with ContentValues and insert(). Read: getAllNotes() via a cursor and raw queries. Update: updateNote() with a WHERE clause. Delete: deleteNote(). Learning SQLite types and auto-increment IDs took practice.

# What challenges did you face in maintaining data persistence?
I was initially confused about onCreate() vs onUpgrade(). Learning to manage cursors and when to open/close connections helped. Realizing the DB file is stored on internal storage makes the lifecycle clearer.

# What improvements would you add in future versions?
I will maybe add a search bar to filter notes, and improve the RecyclerView's empty state. Adding subtle animations and better error handling would help, and I want to try Room later.
