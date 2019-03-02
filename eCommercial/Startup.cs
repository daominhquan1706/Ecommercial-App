using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(eCommercial.Startup))]
namespace eCommercial
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
            // Any connection or hub wire up and configuration should go here
            app.MapSignalR();
        }
    }
}
