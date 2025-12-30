//
//  LongOrderMapviewViewController.h
//  BJPinChe
//
//  Created by yc on 14-12-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"
#import "BMKPinAnnotationView.h"
@interface LongOrderMapviewViewController : UIViewController<BMKMapViewDelegate>

{
    BMKMapView *myMapview;
}
@property(strong, nonatomic)STAcceptableLongOrderDetailInfo * longOrderDetailInfo;

@end
