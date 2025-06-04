<h1 align="center">🎥Hamlet Movie</h1>

---------------------------
- **Infinite Movie List**  
  Browse the latest movies with seamless infinite scrolling.




https://github.com/user-attachments/assets/722d8cfe-565c-4cec-97a2-7900fcea5c51



- **Auto-complete Search**  
  Search for movies with a fast, real-time suggestion dropdown.


https://github.com/user-attachments/assets/f03b5c4d-b089-4089-80cf-74f57bc9392c




- **Movie Detail View**  
  A simplified detail screen for each movie including:
    - Poster
    - Cover
    - Title, Release date
    - Popularity
    - Votes
    - Language

https://github.com/user-attachments/assets/9724b67e-78af-41d5-8b2a-9091cffd62ef



- The app is designed to work properly even when there is no internet connection.


https://github.com/user-attachments/assets/407c6d36-ab3f-4f2f-bcd3-03403ca7b6b1



---------------------------









Model-View-ViewModel (i.e. MVVM) is a client application architecture template proposed by John Gossman as an alternative to MVC and MVP patterns when using Data Binding technology. Its concept separates data presentation logic from business logic by moving it into a particular class for a clear distinction.

![MVVM3](https://user-images.githubusercontent.com/1812129/68319232-446cf900-00be-11ea-92cf-cad817b2af2c.png)


**Why Promoting MVVM VS MVP:**
- The ViewModel has Built-in LifeCycleOwerness; however, the Presenter does not, and you have to take this responsibility on your side.
- ViewModel doesn't have a reference for View; on the other hand, Presenter still holds a reference for View, even if you made it a weak reference.
- ViewModel survives configuration changes, while it is your responsibility to survive the configuration changes in the Presenter's case. (Saving and restoring the UI state)


**MVVM Best Pratice:**
- Avoid references to Views in ViewModels.
- Instead of pushing data to the UI, let the UI observe changes.
- Distribute responsibilities, and add a domain layer if needed.
- Add a data repository as the single-point entry to your data.
- Expose information about the state of your data using a wrapper or another LiveData.
- Consider edge cases, leaks, and how long-running operations can affect the instances in your architecture.
- Don’t put logic in the ViewModel that is critical to saving a clean state or related to data. Any call you make from a ViewModel can be the last one.


**Keep your code clean according to MVVM**
-----------------------------
- Yes, liveData is easy and powerful, but you should know how to use it.
- For live date which will emit data stream, it has to be in your
  data layer and don't inform those observables anything else like
  in which thread those will consume, cause it is another
- For live-data that emits UI binding events, it must be in your ViewModel Layer.
- Observers in UI Consume and react to live data values and bind them.
  responsibility, and according to `Single responsibility principle`
  in `SOLID (object-oriented design)` , so don't break this concept by
  mixing the responsibilities.

![mvvm2](https://user-images.githubusercontent.com/1812129/68319008-e9d39d00-00bd-11ea-9245-ebedd2a2c067.png)
