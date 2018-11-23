## Notes ##
### Submission #2 Notes ###
Per submission #1 revision requests, the following fixes have been made.
- Fix: VideoPlayBack state is passed in onSaveInstanceState and properly set
- Fix: API check has been added in onPause / onStop for VideoPlayer fragment
- Fix: Back navigation now includes individual RecipeSteps. 
User flow is now ( Select Recipe ) -> ( Select Recipe Step ) -> ( View Recipe Step )-n, where n is an ID of a new recipe step to be show.

### Submission #1 Notes ###
- Within the UI, network request status updates aren't handled precisely. 
Specifically, the user may open the app, and very briefly notice the "network failed" screen. 
This is probably due to the ViewModel LiveData not being updated frequently enough. 
In reality, this check for network progress and status should be handled in the data layer ( Repository ) and passed up to the UI layer via the RepositoryResponse and not just simply in a generic "getError" method. 
- When running the instrumentation tests, ensure that the application has been installed first before executing the instrumentation tests. My guess is that when executing individual tests, a fresh APK install isn't executed before beginning the tests.t 

## App Development in Reflection
This app has been such an experience. 
- Should have started with Dagger2 from the onset. This would have made testing a lot easier. 
- Should have organized packages by feature
- Should incorporate Roboelectric and UIAutomator for more complete UI testing capabilities, especially when testing widgets. 
- Should have started with UnitTests from the onset. Would have saved the trouble of debugging weird network errors and such.  
- Using ViewModels and Repository have been very helpful, however still need to figure out someway to test both. 
- Should invest more time in managing data state between Room and the network within Repository.  
- Utilize build variants to separate debug and release packages
- In hindsight, using Moshi and OkHttp3 is simple and light, however incorporating Gson and Retrofit could be better due to wider community documentation and (apparent) finer control concerning  request / response visibility.

## Credits and Attributions
'Bake Tools' Logo - Created by Linda from the Noun Project
