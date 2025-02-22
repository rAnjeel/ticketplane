package controlleurs;

import mg.itu.prom16.Annotations.*;
import mg.itu.prom16.*;

@Controller
public class HomeController {
    
    @Get
    @Url("/")
    public ModelView index() {
        ModelView modelView = new ModelView();
        modelView.setUrl("index.jsp");
        return modelView;
    }
} 