import { NextApiRequest, NextApiResponse } from 'next';
import { cancelRentalRequest } from 'src/app/api/repos/[owner]/[repo]/services/rentalService';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'POST') {
        const { id } = req.query;

        try {
            const result = await cancelRentalRequest(id as string);
            return res.status(200).json(result);
        } catch (error) {
            return res.status(500).json({ message: 'Error cancelling rental request', error });
        }
    } else {
        res.setHeader('Allow', ['POST']);
        return res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}