//
//  Sizer.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//
import Foundation
import SwiftUI

class Sizer: ObservableObject {
    
    static let shared = Sizer()
    
    init(custom: CGSize = .zero) { self.customSize = custom }
    
    @Published var content: CGSize = .zero
    
    private var customSize: CGSize = .zero
    private var proportionalContent: CGSize {
        get {
            let proportionalW = (idealSize.width * content.height) / idealSize.height
            let proportionalH = (idealSize.height * content.width) / idealSize.width
            
            return CGSize(
                width: proportionalW > content.width ? content.width : proportionalW,
                height: proportionalH > content.height ? content.height : proportionalH
            )
        }
    }
    var idealSize = CGSize(width: 390, height: 844)
    
    func scaled(w: CGFloat = 0, h: CGFloat = 0, proportional: Bool = true ) -> CGSize {
        let contentW = customSize != .zero
        ? customSize.width
        : (!proportional) ? content.width : proportionalContent.width
        let contentH = customSize != .zero
        ? customSize.height
        : (!proportional) ? content.height : proportionalContent.height
        
        let relativeW = w > 0 ? (w * contentW) / idealSize.width : 0
        let relativeH = h > 0 ? (h * contentH) / idealSize.height : 0
        
        return CGSize(width: relativeW, height: relativeH)
    }
}

struct SizePreferenceKey: PreferenceKey {
    static var defaultValue: CGSize = .zero
    static func reduce(value: inout CGSize, nextValue: () -> CGSize) {
        value = nextValue()
    }
}

struct SizeModifier: ViewModifier {
    private var sizeView: some View {
        GeometryReader { geometry in
            Color.clear
                .preference(key: SizePreferenceKey.self, value: geometry.size)
        }
    }
    func body(content: Content) -> some View {
        content.background(sizeView)
    }
}
