import SwiftUI
import SwiftUIRouter
import shared

struct ContentView: View
{
	var body: some View
    {
        Router
        {
            Route
            {
                GameScene()
            }
        }
	}
}
