//
//  Cus_OrderDrvPosViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/15/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
@interface Cus_OrderDrvPosViewController : SuperViewController <OrderSvcDelegate, BMKRouteSearchDelegate, BMKMapViewDelegate,BMKLocationServiceDelegate>
{
    IBOutlet BMKMapView *               _MapView;
    BMKRouteSearch *                    mRouteSearch;
    
    BMKLocationService *                locService;

}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

- (IBAction)onClickedBack:(id)sender;


@end
