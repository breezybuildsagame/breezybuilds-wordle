import SwiftUI
import SwiftUIRouter
import shared

struct ContentView: View
{
    @ObservedObject private var sizer = Sizer.shared
    @ObservedObject private var appModalHandler = AppModalViewHandler.shared
    @EnvironmentObject private var navigator: Navigator
    
    @StateObject private var sceneDimensions = SceneDimensions.shared
    
	var body: some View
    {
        ZStack
        {
            SizerView()
            
            SwitchRoutes
            {
                Route(AppRoute.game.name)
                {
                    GameScene()
                }
                Route
                {
                    GameScene()
                }
            }
            .transition(.opacity)
            
            if (appModalHandler.appModalIsShowing)
            {
                ZStack(alignment: .center)
                {
                    Color.clear
                    
                    appModalHandler.modalContent()
                }
            }
        }
        .onAppear()
        {
            SceneNavigationHandler.shared.setUp(navigator: navigator)
            appModalHandler.setUp()
        }
        .environmentObject(sceneDimensions)
        .onReceive(sizer.$content)
        { contentSize in
            sceneDimensions.setDimensions(
                width: sizer.scaled(w: sizer.idealSize.width).width,
                height: sizer.scaled(h: sizer.idealSize.height).height,
                screenSize: CGSize(width: contentSize.width, height: contentSize.height)
            )
        }
        .animation(.easeInOut, value: navigator.path)
	}
    
    private func SizerView() -> some View {
        Color.ui.primary
            .modifier(SizeModifier())
            .onPreferenceChange(SizePreferenceKey.self) { sizer.content = $0 }
            .edgesIgnoringSafeArea(.all)
    }
}
