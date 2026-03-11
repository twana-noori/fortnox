import { NextApiRequest, NextApiResponse } from 'next';
import { parseRentalRequest } from 'src/app/api/utils/rentalUtils';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'POST') {
        try {
            const rentalData = await parseRentalRequest(req.body);
            // Further processing of rentalData can be done here
            res.status(200).json({ success: true, data: rentalData });
        } catch (error) {
            res.status(400).json({ success: false, message: error.message });
        }
    } else {
        res.setHeader('Allow', ['POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}