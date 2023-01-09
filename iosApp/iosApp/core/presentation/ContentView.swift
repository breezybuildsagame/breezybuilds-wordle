import SwiftUI
import SwiftUIRouter
import shared

struct ContentView: View
{
    @ObservedObject private var sizer = Sizer.shared
    @EnvironmentObject private var navigator: Navigator
    
    @StateObject private var sceneDimensions = SceneDimensions.shared
    
	var body: some View
    {
        ZStack
        {
            SizerView()
            SwitchRoutes
            {
                Route
                {
                    GameScene()
                }
            }
            .transition(.opacity)
        }
        .environmentObject(sceneDimensions)
        .onReceive(sizer.$content)
        { contentSize in
            sceneDimensions.setDimensions(width: contentSize.width, height: contentSize.height)
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
