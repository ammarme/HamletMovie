<h1 align="center">🎥Hamlet Movie</h1>

---------------------------
- **Infinite Movie List**  
  Browse the latest movies with seamless infinite scrolling.


https://github.com/user-attachments/assets/571ada28-d925-4668-8b14-ffa7af048469




- **Search feature with an auto-complete function**  
  Search for movies with a fast, real-time suggestion dropdown.

https://github.com/user-attachments/assets/5a2cf0c5-66cf-468d-9348-93a000de6e5a



- **Movie Detail View**  
  A simplified detail screen for each movie, including:
    - Poster
    - Cover
    - Title, Release date
    - Popularity
    - Votes
    - Language


https://github.com/user-attachments/assets/4a2a1c8b-775a-486b-ab75-468c7318898e




- The app is designed to work properly even when there is no internet connection.



https://github.com/user-attachments/assets/224b5c9f-b37d-4c5c-be5c-569b06d2f2c5





---------------------------





Model-View-ViewModel (MVVM) is a client application architecture template proposed by John Gossman as an alternative to the MVC and MVP patterns when using Data Binding technology. Its concept separates data presentation logic from business logic by moving it into a specific class, allowing for a clear distinction.

![MVVM3](https://user-images.githubusercontent.com/1812129/68319232-446cf900-00be-11ea-92cf-cad817b2af2c.png)


**Why Promoting MVVM VS MVP:**
- The ViewModel has Built-in LifeCycleOwerness; however, the Presenter does not, and you have to take this responsibility on your side.
- ViewModel doesn't have a reference for View; on the other hand, Presenter still holds a reference for View, even if you made it a weak reference.
- ViewModel survives configuration changes, while it is your responsibility to survive the configuration changes in the Presenter's case. (Saving and restoring the UI state)


**MVVM Best Practice:**
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
- For a live date that will emit a data stream, it has to be in your
  data layer and don't inform those observables anything else, like which thread they will consume, cause it is another
- For live data that emits UI binding events, it must be in your ViewModel Layer.
- Observers in UI Consume and react to live data values and bind them.
  responsibility, and according to `Single responsibility principle`
  in `SOLID (object-oriented design)` , so don't break this concept by
  mixing the responsibilities.

![mvvm2](https://user-images.githubusercontent.com/1812129/68319008-e9d39d00-00bd-11ea-9245-ebedd2a2c067.png)
