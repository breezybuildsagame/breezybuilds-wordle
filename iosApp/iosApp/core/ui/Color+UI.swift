//
//  Extension+Color.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Color
{
    static let ui = Color.UI()
    
    struct UI
    {
        let primary = Color("color.primary")
        let onPrimary = Color("color.on_primary")
    }
}
