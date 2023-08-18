import SwiftUI
import SwiftUIRouter
import shared

struct ContentView: View
{
    @ObservedObject private var sizer = Sizer.shared
    @ObservedObject private var appModalHandler = AppModalViewHandler.shared
    @ObservedObject private var appSheetHandler = AppSheetViewHandler.shared
    
    private var gameSceneHandler: GameSceneHandler? = nil
    
    @EnvironmentObject private var navigator: Navigator
    
    @StateObject private var sceneDimensions = SceneDimensions.shared
    
    @State private var showingSheet = false
    
    init(
        appModalHandler: AppModalViewHandler? = nil,
        appSheetHandler: AppSheetViewHandler? = nil,
        gameSceneHandler: GameSceneHandler? = nil
    )
    {
        self.appModalHandler = appModalHandler ?? self.appModalHandler
        self.appSheetHandler = appSheetHandler ?? self.appSheetHandler
        self.gameSceneHandler = gameSceneHandler
    }
    
	var body: some View
    {
        ZStack
        {
            SizerView()
            
            SwitchRoutes
            {
                Route(AppRoute.game.name)
                {
                    GameScene(handler: gameSceneHandler)
                }
                Route
                {
                    GameScene(handler: gameSceneHandler)
                }
            }
            .transition(.opacity)
            
            if (appModalHandler.appModalIsShowing)
            {
                ZStack(alignment: .center)
                {
                    Color.black.opacity(0.8)
                    
                    appModalHandler.modalContent()
                }
            }
        }
        .onAppear()
        {
            SceneNavigationHandler.shared.setUp(navigator: navigator)
            appModalHandler.setUp()
            appSheetHandler.setUp()
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
        .onChange(
            of: appSheetHandler.appSheetIsShowing,
            perform: { isShowing in showingSheet = isShowing }
        )
        .sheet(
            isPresented: $showingSheet,
            onDismiss: {
                appSheetHandler.onSheetShouldHide(animationDuration: 300)
            },
            content: {
                ZStack
                {
                    Color.ui.surface
                    appSheetHandler.sheetContent()
                }
                .edgesIgnoringSafeArea(.all)
                .environmentObject(sceneDimensions)
            })
	}
    
    private func SizerView() -> some View {
        Color.ui.primary
            .modifier(SizeModifier())
            .onPreferenceChange(SizePreferenceKey.self) { sizer.content = $0 }
            .edgesIgnoringSafeArea(.all)
    }
}
