
## Notes
- Within the UI, network request status updates aren't handled precisely. 
Specifically, the user may open the app, and very briefly notice the "network failed" screen. 
This is probably due to the ViewModel LiveData not being updated frequently enough. 
In reality, this check for network progress and status should be handled in the data layer ( Repository ) and passed up to the UI layer via the RepositoryResponse and not just simply in a generic "getError" method. 

## App Development in Reflection
This app has been such an experience. 
- Should have started with Dagger2 from the onset. This would have made testing a lot easier. 
- Should have organized by feature
- Should have started with UnitTests from the onset. Would have saved the trouble of debugging weird network errors and such.  
- Using ViewModels and Repository have been very helpful, however still need to figure out someway to test both. 
- Should invest more time in managing data state between Room and the network within Repository.  
- Utilize build variants to separate debug and release packages

## Credits and Attributions
'Bake Tools' Logo - Created by Linda from the Noun Project
